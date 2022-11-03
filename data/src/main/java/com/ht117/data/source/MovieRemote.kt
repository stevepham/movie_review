package com.ht117.data.source

import android.util.Log
import com.ht117.data.model.*
import com.ht117.data.response.MovieResponse
import com.ht117.data.response.Response
import com.ht117.data.response.toMovie
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

interface IMovieSource {
    suspend fun requestNowPlaying(): Flow<State<List<Movie>>>
    suspend fun requestPopular(page: Int): State<List<Movie>>
    suspend fun requestDetail(movieId: Long): Flow<State<Movie>>
}

class MovieRemote(private val api: HttpClient): IMovieSource {

    override suspend fun requestNowPlaying() = flow {
        emit(State.Loading)
        val networkResponse = api.get(urlString = "${RemoteConfig.BaseHost}/movie/now_playing?language=en-US}")
        if (networkResponse.status.value in 200..299) {
            val body = networkResponse.body<Response<List<MovieResponse>>>()
            val result = body.results.map { it.toMovie() }
            if (result.isNotEmpty()) {
                emit(State.Success(result))
            } else {
                emit(State.Failed(EMPTY))
            }
        } else {
            emit(State.Failed(NET_ERR))
        }
    }.catch { e ->
        Log.d("Debug", "Exception ${e.message} ${e.printStackTrace()}")
        when (e) {
            is UnknownHostException -> emit(State.Failed(NET_ERR))
            else -> emit(State.Failed(UNKNOWN))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun requestPopular(page: Int): State<List<Movie>> {
        return withContext(Dispatchers.IO) {
            try {
                val networkResponse = api.get(urlString = "${RemoteConfig.BaseHost}/movie/popular?language=en-US")
                if (networkResponse.status.value in 200..299) {
                    val response = networkResponse.body<Response<List<MovieResponse>>>()
                    val result = response.results.map {
                        val movieResponse = api.get(urlString = "${RemoteConfig.BaseHost}/movie/${it.id}?language=en-US").body<MovieResponse>()
                        movieResponse.toMovie()
                    }
                    if (result.isNotEmpty()) {
                        State.Success(result)
                    } else {
                        State.Failed(EMPTY)
                    }
                } else {
                    State.Failed(EMPTY)
                }
            } catch (nexp: UnknownHostException) {
                State.Failed(NET_ERR)
            } catch (exp: Exception) {
                State.Failed(UNKNOWN)
            }
        }
    }

    override suspend fun requestDetail(movieId: Long) = flow {
        emit(State.Loading)
        val response = api.get("${RemoteConfig.BaseHost}/movie/${movieId}?language=en-US").body<MovieResponse>()
        emit(State.Success(response.toMovie()))
    }.catch { e ->
        when (e) {
            is UnknownHostException -> emit(State.Failed(NET_ERR))
            else -> emit(State.Failed(UNKNOWN))
        }
    }.flowOn(Dispatchers.IO)
}