package com.ht.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.ht.movie.ui.screen.home.IMovieCallback
import com.ht117.app.R
import com.ht117.app.databinding.ItemPosterBinding
import com.ht117.data.model.Movie
import com.ht117.data.model.getPosterUrl

class PosterAdapter(private val callback: IMovieCallback? = null): RecyclerView.Adapter<PosterAdapter.PosterHolder>() {

    private val items = mutableListOf<Movie>()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPosterBinding.inflate(inflater, parent, false)
        return PosterHolder(binding)
    }

    override fun onBindViewHolder(holder: PosterHolder, position: Int) {
        holder.bindView(items[position])
    }

    fun addItems(newItems: List<Movie>) {
        val differ = MovieDiffer(items, newItems)
        val result = DiffUtil.calculateDiff(differ)
        items.clear()
        items.addAll(newItems)
        result.dispatchUpdatesTo(this)
    }

    inner class PosterHolder(private val binding: ItemPosterBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(movie: Movie) = with(binding) {
            root.load(movie.getPosterUrl()) {
                placeholder(R.drawable.ic_movie)
                networkCachePolicy(CachePolicy.ENABLED)
                diskCachePolicy(CachePolicy.ENABLED)
            }

            root.setOnClickListener {
                callback?.invoke(movie.id)
            }
        }
    }
}