package com.ht117.data.response

import com.ht117.data.model.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    val id: Long,
    val name: String
)

@Serializable
data class MovieResponse(
    val id: Long,
    @SerialName("poster_path") val poster: String,
    val title: String,
    val overview: String,
    @SerialName("vote_average") val voteAverage: Float,
    @SerialName("release_date") val releaseDate: String = "",
    val genres: List<Genre> = emptyList(),
    val runtime: Int = 0
)

@Serializable
data class Response<out T>(
    val page: Int,
    val results: T,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResult: Int
)

fun MovieResponse.toMovie(): Movie {
    return Movie(id = id, title = title, poster = poster, overview = overview, runtime = runtime,
        rating = voteAverage, releaseDate = releaseDate, genres = genres.map { it.name })
}