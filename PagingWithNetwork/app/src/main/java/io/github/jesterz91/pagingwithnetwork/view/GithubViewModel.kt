package io.github.jesterz91.pagingwithnetwork.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import io.github.jesterz91.pagingwithnetwork.api.Repo
import io.github.jesterz91.pagingwithnetwork.paging.GithubDataSource
import io.github.jesterz91.pagingwithnetwork.paging.GithubDataSourceFactory

class GithubViewModel(githubDataSourceFactory: GithubDataSourceFactory) : ViewModel() {

    val liveDataSource: LiveData<PageKeyedDataSource<Int, Repo>>

    val repoPagedList: LiveData<PagedList<Repo>>

    init {

        val pageConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(GithubDataSource.page_size)
            .build()

        liveDataSource = githubDataSourceFactory.liveDataSource
        repoPagedList = LivePagedListBuilder(githubDataSourceFactory, pageConfig).build()

    }
}