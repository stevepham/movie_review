package com.ht.movie.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import com.ht.movie.ext.hide
import com.ht.movie.ext.show
import com.ht117.app.R
import com.ht117.app.databinding.ViewStateBinding
import com.ht117.data.model.*

class StateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var binding = ViewStateBinding.inflate(LayoutInflater.from(context), this, true)

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding.run {
            ivOop.setOnClickListener { performClick() }
            tvMsg.setOnClickListener { performClick() }
        }
    }

    fun showLoading() {
        isVisible = true
        binding.run {
            ivOop.hide()
            tvMsg.hide()
            loader.show()
        }
    }

    fun showMessage(code: ErrCode) {
        isVisible = true
        binding.run {
            loader.hide()
            ivOop.show()
            tvMsg.show()
            when (code) {
                NET_ERR -> {
                    tvMsg.setText(R.string.network_err_msg)
                }

                UNKNOWN -> {
                    tvMsg.setText(R.string.unknown_msg)
                }

                EMPTY -> {
                    tvMsg.setText(R.string.empty_msg)
                }

                INVALIDATE -> {
                    tvMsg.setText(R.string.invalid_msg)
                }
            }
        }
    }
}