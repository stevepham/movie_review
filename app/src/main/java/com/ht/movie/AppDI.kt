package com.ht.movie

import com.ht.movie.ui.screen.detail.DetailViewModel
import com.ht.movie.ui.screen.home.HomeViewModel
import com.ht117.data.dataModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}

val allModule = listOf(appModule, dataModule)