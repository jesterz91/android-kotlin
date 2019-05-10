package io.github.jesterz91.pagingwithnetwork.view

import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import io.github.jesterz91.pagingwithnetwork.api.GithubApi
import io.github.jesterz91.pagingwithnetwork.api.Repo
import io.github.jesterz91.pagingwithnetwork.paging.GithubDataSourceFactory
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class GithubViewModel(
    private val compositeDisposable: CompositeDisposable,
    private val githubApi: GithubApi
) : ViewModel() {

    val repoPagedList: Observable<PagedList<Repo>>

    init {

        val githubDataSourceFactory = GithubDataSourceFactory(compositeDisposable, githubApi)

        val pageSize = 10

        val pageConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize) // 기본값 : page size * 3
            .setPrefetchDistance(pageSize / 3) // 기본값 : page size
            .build()

        repoPagedList = RxPagedListBuilder(githubDataSourceFactory, pageConfig)
            .setFetchScheduler(Schedulers.io())
            .buildObservable()
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}