package io.github.jesterz91.pagingwithnetwork.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.jesterz91.pagingwithnetwork.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        github.setOnClickListener {
            startActivity<GithubActivity>()
        }

        reddit.setOnClickListener {
            startActivity<RedditActivity>()
        }
    }
}
