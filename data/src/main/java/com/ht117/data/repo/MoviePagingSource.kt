package com.ht117.data.repo

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ht117.data.model.Movie
import com.ht117.data.model.State
import com.ht117.data.source.IMovieSource

class MoviePagingSource(private val source: IMovieSource): PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { pos ->
            val anchorPage = state.closestPageToPosition(pos)
            anchorPage?.prevKey?.plus(1)?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val nextPage = params.key?: 1
            when (val response = source.requestPopular(nextPage)) {
                is State.Success -> {
                    LoadResult.Page(data = response.data, prevKey = null, nextKey = nextPage + 1)
                }
                else -> {
                    LoadResult.Error(Exception("Failed response"))
                }
            }
        } catch (exp: Exception) {
            LoadResult.Error(exp)
        }
    }
}