package io.github.jesterz91.koin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.github.jesterz91.koin.component.MySimplePresenter
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    // inject() 를 통해 의존성 주입
    private val firstPresenter : MySimplePresenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView.text = firstPresenter.sayHello()
    }
}
