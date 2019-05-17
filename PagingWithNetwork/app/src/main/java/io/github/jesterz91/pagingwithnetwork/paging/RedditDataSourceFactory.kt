package io.github.jesterz91.pagingwithnetwork.paging

import androidx.paging.DataSource
import io.github.jesterz91.pagingwithnetwork.api.RedditApi
import io.github.jesterz91.pagingwithnetwork.api.RedditPost
import io.reactivex.disposables.CompositeDisposable

class RedditDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val redditApi: RedditApi
) : DataSource.Factory<String, RedditPost>() {

    override fun create(): DataSource<String, RedditPost> {
        return RedditDataSource(compositeDisposable, redditApi)
    }
}