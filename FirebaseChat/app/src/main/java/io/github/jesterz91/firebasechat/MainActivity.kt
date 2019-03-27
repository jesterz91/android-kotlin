package io.github.jesterz91.firebasechat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.InputFilter
import android.view.Menu
import android.view.MenuItem
import com.crashlytics.android.Crashlytics
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.appinvite.AppInvite
import com.google.android.gms.appinvite.AppInviteInvitation
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity(), AnkoLogger {

    // Firebase child
    private val MESSAGES_CHILD = "messages"

    // Firebase Remote Config KEY
    private val MESSAGE_LENGTH = "message_length"

    // Firebase Invites REQUEST CODE
    private val REQUEST_INVITE = 1000

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val firebaseDatabaseRef: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().reference
    }

    private val firebaseRemoteConfig: FirebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance()
    }

    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var firebaseAdapter: FirebaseAdapter

    private lateinit var userName: String
    private lateinit var photoUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this@MainActivity) {
                toast("Google Play Services error")
            }
            .addApi(Auth.GOOGLE_SIGN_IN_API)
            .addApi(AppInvite.API)
            .build()

        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser

        // 로그인 검사
        if (firebaseUser == null) {
            startActivity(Intent(this@MainActivity, SignInActivity::class.java))
            finish()
            return
        } else {
            userName = firebaseUser.displayName!!
            photoUrl = firebaseUser.photoUrl.toString()
        }

        // 메시지 전송 이벤트 처리
        send_button.setOnClickListener {

            val chatMessage = ChatMessage(
                text = message_edit.text.toString(),
                name = userName,
                photoUrl = photoUrl
            )

            // 데이터베이스 "Message" child 를 만들고 chatMessage 객체 삽입
            firebaseDatabaseRef.child(MESSAGES_CHILD)
                .push()
                .setValue(chatMessage)

            // 입력창 비우기
            message_edit.text.clear()

        }

        // 메시지 전체에 대한 내용을 얻는 쿼리
        val query: Query = firebaseDatabaseRef.child(MESSAGES_CHILD)

        // 리사이클러 뷰 옵션에 쿼리 설정
        val options = FirebaseRecyclerOptions.Builder<ChatMessage>()
            .setQuery(query, ChatMessage::class.java)
            .build()

        // 리사이클러뷰 어댑터 생성
        firebaseAdapter = FirebaseAdapter(options)

        // 리사이클러뷰 설정
        message_recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = firebaseAdapter
        }

        // Remote Config 개발자 모드 활성화
        val firebaseRemoteConfigSettings = FirebaseRemoteConfigSettings.Builder()
            .setDeveloperModeEnabled(true)
            .build()

        // Remote Config 기본값 설정
        val defaultConfigMap = HashMap<String, Any>()
        defaultConfigMap.apply {
            put(MESSAGE_LENGTH, 10L)
        }

        // Remote Config 설정
        firebaseRemoteConfig.apply {
            setConfigSettings(firebaseRemoteConfigSettings)
            setDefaults(defaultConfigMap)
        }

        // Remote Config 반영
        fetchConfig()

    }

    private fun fetchConfig() {
        // Remote Config 반영 주기
        var cacheExpiration = 3600L

        if (firebaseRemoteConfig.info.configSettings.isDeveloperModeEnabled) {
            cacheExpiration = 0L
        }
        // Remote Config 처리
        firebaseRemoteConfig.fetch(cacheExpiration)
            .addOnSuccessListener {
                debug { "Success fetching config" }
                firebaseRemoteConfig.activateFetched()
                applyRetrievedLengthLimit()
            }
            .addOnFailureListener {
                warn { "Error fetching config : ${it.message}" }
                applyRetrievedLengthLimit()
            }
    }

    // Remote Config 에서 얻은 값으로 EditText 입력 길이 제한
    private fun applyRetrievedLengthLimit() {
        val messageLength = firebaseRemoteConfig.getLong(MESSAGE_LENGTH)
        message_edit.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(messageLength.toInt()))
        debug { "메시지 길이 : $messageLength" }
    }

    // 앱 초대
    private fun sendInvitation() {
        val intent = AppInviteInvitation.IntentBuilder("초대 제목")
            .setMessage("채팅앱에 초대합니다.")
            .setCallToActionText("채팅에 참여하기")
            .build()
        startActivityForResult(intent, REQUEST_INVITE)
    }

    // Realtime Database 모니터링 시작
    override fun onStart() {
        super.onStart()
        firebaseAdapter.startListening()
    }

    // Realtime Database 모니터링 종료
    override fun onStop() {
        super.onStop()
        firebaseAdapter.stopListening()
    }

    // 옵션 메뉴 설정
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    // 옵션 메뉴 이벤트 처리
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            // 로그아웃 이벤트
            R.id.sign_out -> {
                firebaseAuth.signOut()
                Auth.GoogleSignInApi.signOut(googleApiClient)
                userName = ""
                toast("로그아웃 되었습니다.")
                startActivity(Intent(this@MainActivity, SignInActivity::class.java))
                finish()
                return true
            }
            // 앱 초대 이벤트
            R.id.invitation -> {
                sendInvitation()
                return true
            }
            // 에러 발생
            R.id.crash -> {
                Crashlytics.getInstance().crash()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_INVITE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val ids = AppInviteInvitation.getInvitationIds(resultCode, data)
                    info { "INVITE IDS : $ids" }
                } else {

                }
            }
        }
    }
}
