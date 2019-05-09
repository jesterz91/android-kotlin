package io.github.jesterz91.pagingwithnetwork.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import io.github.jesterz91.pagingwithnetwork.api.GithubApi
import io.github.jesterz91.pagingwithnetwork.api.Repo

class GithubDataSourceFactory(private val githubApi: GithubApi) : DataSource.Factory<Int, Repo>() {

    val liveDataSource = MutableLiveData<PageKeyedDataSource<Int, Repo>>()

    override fun create(): DataSource<Int, Repo> {
        val source = GithubDataSource(githubApi)
        liveDataSource.postValue(source)
        return source
    }
}