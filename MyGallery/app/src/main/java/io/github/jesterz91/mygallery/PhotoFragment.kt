package io.github.jesterz91.mygallery


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_photo.*

// 컴파일 시간의 상수초기화는 프리미티브형(String, Int Long, Double 등)만 가능
private const val ARG_URI = "uri"

class PhotoFragment : Fragment() {
    private var uri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // arguments 프로퍼티에 저장된 ARG_URI 값을 가져옴
        arguments?.let {
            uri = it.getString(ARG_URI)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this)
            .load(uri)
            .into(imageView)
    }

    // 인자로 uri 값을 전달받아 프래그먼트를 생성하는 팩토리 메서드
    companion object {
        @JvmStatic
        fun newInstance(uri: String) =
            PhotoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_URI, uri)
                }
            }
    }
}
