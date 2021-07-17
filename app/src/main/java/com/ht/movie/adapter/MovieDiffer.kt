package com.ht.movie.adapter

import androidx.recyclerview.widget.DiffUtil
import com.ht117.data.model.Movie

class MovieDiffer(private val oldItems: List<Movie>,
                  private val newItems: List<Movie>): DiffUtil.Callback() {

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
        val oldItem = oldItems[oldPos]
        val newItem = newItems[newPos]

        return oldItem == newItem
    }

    override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
        val oldItem = oldItems[oldPos]
        val newItem = newItems[newPos]

        return oldItem.id == newItem.id
                && oldItem.title == newItem.title
    }
}