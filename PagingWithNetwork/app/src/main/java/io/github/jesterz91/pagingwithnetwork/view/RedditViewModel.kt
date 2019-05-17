package io.github.jesterz91.pagingwithnetwork.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.github.jesterz91.pagingwithnetwork.api.RedditApi
import io.github.jesterz91.pagingwithnetwork.api.RedditPost
import io.github.jesterz91.pagingwithnetwork.paging.RedditDataSourceFactory
import io.reactivex.disposables.CompositeDisposable

class RedditViewModel(
    private val compositeDisposable: CompositeDisposable,
    private val redditApi: RedditApi
) : ViewModel() {

    val redditPagedList: LiveData<PagedList<RedditPost>>

    init {

        val redditDataSourceFactory = RedditDataSourceFactory(compositeDisposable, redditApi)

        val pageSize = 10

        val pageConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize) // 기본값 : page size * 3
            .setPrefetchDistance(pageSize / 3) // 기본값 : page size
            .build()

        redditPagedList = LivePagedListBuilder(redditDataSourceFactory, pageConfig).build()

    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}