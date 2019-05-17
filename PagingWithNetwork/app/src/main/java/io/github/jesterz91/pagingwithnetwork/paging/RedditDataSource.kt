package io.github.jesterz91.pagingwithnetwork.paging

import androidx.paging.ItemKeyedDataSource
import io.github.jesterz91.pagingwithnetwork.api.RedditApi
import io.github.jesterz91.pagingwithnetwork.api.RedditPost
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info

class RedditDataSource(
    private val compositeDisposable: CompositeDisposable,
    private val redditApi: RedditApi
) : ItemKeyedDataSource<String, RedditPost>(), AnkoLogger {

    private val subreddit = "ProgrammerHumor"

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<RedditPost>) {

        info { "loadInitial params : ${params.requestedInitialKey}, ${params.requestedLoadSize}" }
        compositeDisposable.add(
            redditApi.getTop(subreddit, params.requestedLoadSize)
                .subscribe({
                    val redditPosts = it.data.children
                        .map { children -> children.data }
                    callback.onResult(redditPosts)
                }, {
                    error { it.message }
                })
        )
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<RedditPost>) {

        info { "loadAfter params : ${params.key}, ${params.requestedLoadSize}" }
        compositeDisposable.add(
            redditApi.getTopAfter(subreddit, params.key, params.requestedLoadSize)
                .subscribe({
                    val redditPosts = it.data.children
                        .map { children -> children.data }
                    callback.onResult(redditPosts)
                }, {
                    error { it.message }
                })
        )
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<RedditPost>) {

        info { "loadBefore params : ${params.key}, ${params.requestedLoadSize}" }
        compositeDisposable.add(
            redditApi.getTopBefore(subreddit, params.key, params.requestedLoadSize)
                .subscribe({
                    val redditPosts = it.data.children
                        .map { children -> children.data }
                    callback.onResult(redditPosts)
                }, {
                    error { it.message }
                })
        )
    }

    // 항목의 고유 식별자
    override fun getKey(item: RedditPost): String {
        return item.name
    }
}