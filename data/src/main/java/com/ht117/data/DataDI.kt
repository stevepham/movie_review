package com.ht117.data

import com.ht117.data.repo.IMovieRepo
import com.ht117.data.repo.MoviePagingSource
import com.ht117.data.repo.MovieRepoImpl
import com.ht117.data.source.IMovieSource
import com.ht117.data.source.MovieRemote
import com.ht117.data.source.RemoteConfig
import org.koin.dsl.module

val dataModule = module {
    single { RemoteConfig.client() }
    single<IMovieSource> { MovieRemote(get()) }
    single<IMovieRepo> { MovieRepoImpl(get(), get()) }
    single { MoviePagingSource(get()) }
}