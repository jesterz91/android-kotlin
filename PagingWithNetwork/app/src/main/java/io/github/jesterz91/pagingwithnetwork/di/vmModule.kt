package io.github.jesterz91.pagingwithnetwork.di

import io.github.jesterz91.pagingwithnetwork.view.GithubViewModel
import io.github.jesterz91.pagingwithnetwork.view.RedditViewModel
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vmModule = module {

    viewModel {
        GithubViewModel(get(), get())
    }

    viewModel {
        RedditViewModel(get(), get())
    }

    factory {
        CompositeDisposable()
    }
}