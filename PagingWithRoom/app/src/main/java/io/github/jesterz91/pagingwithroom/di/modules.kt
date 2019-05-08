package io.github.jesterz91.pagingwithroom.di

import io.github.jesterz91.pagingwithroom.view.CheeseViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vmModule = module {

    viewModel {
        CheeseViewModel(get())
    }

}