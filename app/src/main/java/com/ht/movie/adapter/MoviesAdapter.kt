package com.ht.movie.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.ht.movie.R
import com.ht.movie.databinding.ItemMovieBinding
import com.ht.movie.ui.screen.home.IMovieCallback
import com.ht117.data.model.Movie
import com.ht117.data.model.getFormatDate
import com.ht117.data.model.getFormatDuration
import com.ht117.data.model.getPosterUrl

class MoviesAdapter(private val callback: IMovieCallback? = null): PagingDataAdapter<Movie, MoviesAdapter.MovieHolder>(differ) {

    private var maxBindPos: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieBinding.inflate(inflater, parent, false)
        return MovieHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.bindView(getItem(position), position)
    }

    inner class MovieHolder(private val binding: ItemMovieBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindView(movie: Movie?, position: Int) = with(binding) {
            if (movie != null) {
                title.text = movie.title
                releaseDate.text = movie.getFormatDate()
                poster.load(movie.getPosterUrl()) {
                    placeholder(R.drawable.ic_movie)
                    diskCachePolicy(CachePolicy.ENABLED)
                    networkCachePolicy(CachePolicy.ENABLED)
                }

                val isAnimate = if (position > maxBindPos) {
                    maxBindPos = position
                    true
                } else {
                    false
                }

                rating.setScore(movie.rating, isAnimate)

                tvDuration.text = movie.getFormatDuration()

                root.setOnClickListener { callback?.invoke(movie.id) }
                title.setOnClickListener { callback?.invoke(movie.id) }
                releaseDate.setOnClickListener { callback?.invoke(movie.id) }
            }
        }
    }

    companion object {
        val differ = object: DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
                        && oldItem.title == newItem.title
                        && oldItem.poster == newItem.poster
                        && oldItem.releaseDate == newItem.releaseDate
            }

        }
    }
}