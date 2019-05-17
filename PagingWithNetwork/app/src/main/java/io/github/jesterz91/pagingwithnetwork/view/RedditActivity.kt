package io.github.jesterz91.pagingwithnetwork.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.jesterz91.pagingwithnetwork.R
import kotlinx.android.synthetic.main.activity_reddit.*
import org.jetbrains.anko.AnkoLogger
import org.koin.androidx.viewmodel.ext.android.viewModel

class RedditActivity : AppCompatActivity(), AnkoLogger {

    private val viewModel by viewModel<RedditViewModel>()

    private val redditAdapter = RedditAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reddit)

        subredditList.apply {
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(this@RedditActivity, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(this@RedditActivity)
            adapter = redditAdapter
        }

        viewModel.redditPagedList.observe(this, Observer {
            redditAdapter.submitList(it)
        })
    }
}