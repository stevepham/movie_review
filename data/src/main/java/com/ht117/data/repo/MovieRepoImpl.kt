package com.ht117.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ht117.data.model.Movie
import com.ht117.data.model.State
import com.ht117.data.source.IMovieSource
import com.ht117.data.source.MovieRemote
import kotlinx.coroutines.flow.Flow

interface IMovieRepo {
    suspend fun getNowPlaying(): Flow<State<List<Movie>>>

    suspend fun getDetail(movieId: Long): Flow<State<Movie>>

    suspend fun getPopular(): Flow<PagingData<Movie>>
}

class MovieRepoImpl(private val source: IMovieSource,
                    private val moviePaging: MoviePagingSource): IMovieRepo {

    override suspend fun getNowPlaying(): Flow<State<List<Movie>>> {
        return source.requestNowPlaying()
    }

    override suspend fun getDetail(movieId: Long): Flow<State<Movie>> {
        return source.requestDetail(movieId)
    }

    override suspend fun getPopular(): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(20)) { moviePaging }
            .flow
    }
}