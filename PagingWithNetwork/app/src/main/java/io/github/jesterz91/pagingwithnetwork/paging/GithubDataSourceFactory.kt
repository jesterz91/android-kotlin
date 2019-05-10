package io.github.jesterz91.pagingwithnetwork.paging

import androidx.paging.DataSource
import io.github.jesterz91.pagingwithnetwork.api.GithubApi
import io.github.jesterz91.pagingwithnetwork.api.Repo
import io.reactivex.disposables.CompositeDisposable

class GithubDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val githubApi: GithubApi
) : DataSource.Factory<Int, Repo>() {

    override fun create(): DataSource<Int, Repo>
            = GithubDataSource(compositeDisposable, githubApi)
}