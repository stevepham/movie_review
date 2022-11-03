package com.ht.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ht117.app.databinding.ItemGenreBinding

class GenreAdapter: RecyclerView.Adapter<GenreAdapter.GenreHolder>() {

    private val genres = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGenreBinding.inflate(inflater, parent, false)
        return GenreHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreHolder, position: Int) {
        holder.bind(genres[position])
    }

    override fun getItemCount() = genres.size

    fun setData(items: List<String>) {
        if (items.isNotEmpty()) {
            genres.clear()
            genres.addAll(items)
            notifyDataSetChanged()
        }
    }

    inner class GenreHolder(private val binding: ItemGenreBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.root.text = item
        }
    }
}