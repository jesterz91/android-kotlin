package io.github.jesterz91.pagingwithnetwork.paging

import androidx.paging.PageKeyedDataSource
import io.github.jesterz91.pagingwithnetwork.api.GithubApi
import io.github.jesterz91.pagingwithnetwork.api.Repo
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info

class GithubDataSource(
    private val compositeDisposable: CompositeDisposable,
    private val githubApi: GithubApi
) : PageKeyedDataSource<Int, Repo>(), AnkoLogger {

    private val keyWord = "scyllaDB"
    private val firstPage = 1

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Repo>) {
        // parmas.requestedLoadSize 값은 pagedList.config 의 setInitialLoadSizeHint 설정 값
        info { "loadInitial - params.requestedLoadSize : ${params.requestedLoadSize}" }

        compositeDisposable.add(
            githubApi.searchRepos(keyWord, firstPage, params.requestedLoadSize)
                .subscribe({
                    info { "total_count ${it.total_count}" }
                    callback.onResult(it.items, null, firstPage+1)
                }, {
                    error { it.message }
                })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Repo>) {
        // parmas.requestedLoadSize 값은 pagedList.config 의 setPageSize 설정 값
        info { "loadAfter - params(key, requestedLoadSize)  : (${params.key}, ${params.requestedLoadSize})" }

        var nextPageKey: Int? = null
        val requestedItemCount = params.key * params.requestedLoadSize

        compositeDisposable.add(
            githubApi.searchRepos(keyWord, params.key, params.requestedLoadSize)
                .subscribe({
                    if (it.total_count > requestedItemCount) nextPageKey = params.key + 1
                    callback.onResult(it.items, nextPageKey)
                }, {
                    error { it.message }
                })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Repo>) {
        // parmas.requestedLoadSize 값은 pagedList.config 의 setPageSize 설정 값
        info { "loadBefore - params(key, requestedLoadSize)  : (${params.key}, ${params.requestedLoadSize})" }

        var prevPagekey: Int? = null

        compositeDisposable.add(
            githubApi.searchRepos(keyWord, params.key, params.requestedLoadSize)
                .subscribe({
                    if (params.key > firstPage) prevPagekey = params.key - 1
                    callback.onResult(it.items, prevPagekey)
                }, {
                    error { it.message }
                })
        )
    }
}