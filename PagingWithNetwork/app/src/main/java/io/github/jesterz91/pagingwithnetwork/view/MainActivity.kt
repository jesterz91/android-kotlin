package io.github.jesterz91.pagingwithnetwork.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.jesterz91.pagingwithnetwork.R
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), AnkoLogger {

    private val viewModel by viewModel<GithubViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val githubAdapter = GithubAdapter()

        repoList.apply {
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = githubAdapter
        }

        val disposable=  viewModel.repoPagedList
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                githubAdapter.submitList(it)
            }, {
                error { it.message }
            })
    }

}