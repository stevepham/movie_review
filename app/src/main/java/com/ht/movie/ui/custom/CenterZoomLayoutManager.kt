package com.ht.movie.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CenterZoomLayoutManager(
    context: Context?,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : LinearLayoutManager(context, attrs, defStyleAttr, defStyleRes) {

    override fun canScrollVertically() = false

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
        scaleUpCenter()
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        return if (orientation == HORIZONTAL) {
            super.scrollHorizontallyBy(dx, recycler, state).also { scaleUpCenter() }
        } else {
            return 0
        }
    }

    private fun scaleUpCenter() {
        val midpoint = width / 2F
        (0 until childCount).forEach {
            val child = getChildAt(it) as View
            val rate = (Math.min(midpoint, Math.abs(midpoint - (getDecoratedRight(child) + getDecoratedLeft(child)) / 2F))) / midpoint
            val scale = 1F - MAX_SCALE * rate
            child.apply {
                scaleX = scale
                scaleY = scale
            }
        }
    }

    companion object {
        private const val MAX_SCALE = 0.2F
    }
}