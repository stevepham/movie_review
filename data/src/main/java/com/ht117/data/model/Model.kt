package com.ht117.data.model

import java.text.SimpleDateFormat
import java.util.*

typealias ErrCode = Int
const val NET_ERR: ErrCode = 1
const val UNKNOWN: ErrCode = 2
const val EMPTY: ErrCode = 3
const val INVALIDATE: ErrCode = 4


sealed class State<out T> {
    object Init: State<Nothing>()
    object Loading: State<Nothing>()
    data class Success<T>(val data: T): State<T>()
    data class Failed(val errCode: ErrCode = UNKNOWN): State<Nothing>()
}

data class Movie(val id: Long = 0L,
                 val title: String = "",
                 val poster: String = "",
                 val rating: Float = 0F,
                 val releaseDate: String = "",
                 val genres: List<String> = emptyList(),
                 val overview: String = "",
                 val runtime: Int = 0)

fun Movie.getPosterUrl(): String {
    return "https://image.tmdb.org/t/p/original${poster}"
}

fun Movie.getFormatDuration(): String {
    val hour = runtime / 60
    val min = runtime % 60
    return "${hour}h ${min}m"
}

fun Movie.getFormatDate(): String {
    return try {
        val parts = releaseDate.split("-")
        val calendar = Calendar.getInstance()
        calendar.set(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
        val spf = SimpleDateFormat("MMM d, yyyy")
        spf.format(calendar.time)
    } catch (exp: Exception) {
        releaseDate
    }
}