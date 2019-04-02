package io.github.jesterz91.room.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.jesterz91.room.R
import io.github.jesterz91.room.repository.db.Word
import io.github.jesterz91.room.viewmodel.WordViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), InsertDialog.OnAddListener {

    private lateinit var wordViewModel: WordViewModel

    // recyclerView 어댑터
    private val wordAdapter = WordAdapter { deleteWord ->
        // 단어 리스트에서 선택한 단어를 삭제
        wordViewModel.delete(deleteWord)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ViewModel 초기화
        wordViewModel = ViewModelProviders.of(this).get(WordViewModel::class.java)

        // ViewModel 데이터 관찰 및 반영
        wordViewModel.allWords.observe(this, Observer { words ->
            words?.let { wordAdapter.setWords(it) }
        })

        // 리사이클러뷰 설정
        recyclerView.apply {
            setHasFixedSize(true)
            adapter = wordAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        // 단어 추가 버튼
        fab.setOnClickListener {
            // 다이얼로그 설정
            val addDialog = InsertDialog().apply {
                setOnClickListener(this@MainActivity)
            }
            // 다이얼로그 표시
            addDialog.show(supportFragmentManager, "InsertDialog")
        }
    }

    // 단어추가 다이얼로그 콜백
    override fun onAdded(word: String) {
        // DB에 단어 추가
        wordViewModel.insert(Word(word = word))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menuDeleteAll -> {
                // DB에 저장된 단어를 모두삭제
                wordViewModel.deleteAll()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
