package io.github.jesterz91.pagingwithnetwork.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.jesterz91.pagingwithnetwork.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_github.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class GithubActivity : AppCompatActivity(), AnkoLogger {

    private val viewModel by viewModel<GithubViewModel>()

    private val compositeDisposable by inject<CompositeDisposable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github)

        val githubAdapter = GithubAdapter()

        repoList.apply {
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(this@GithubActivity, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(this@GithubActivity)
            adapter = githubAdapter
        }

        val disposable = viewModel.repoPagedList
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                githubAdapter.submitList(it)
            }, {
                error { it.message }
            })

        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

}