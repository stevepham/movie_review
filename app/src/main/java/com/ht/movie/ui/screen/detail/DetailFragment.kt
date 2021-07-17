package com.ht.movie.ui.screen.detail

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.ht.movie.R
import com.ht.movie.adapter.GenreAdapter
import com.ht.movie.databinding.FragmentDetailBinding
import com.ht.movie.ext.hide
import com.ht.movie.ext.show
import com.ht.movie.ui.screen.base.IIntent
import com.ht.movie.ui.screen.base.IState
import com.ht.movie.ui.screen.base.IView
import com.ht117.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

sealed class DetailState: IState {
    object Initialize: DetailState()
    object Invalidate: DetailState()
    data class MovieState(val movie: Flow<State<Movie>>): DetailState()
}

sealed class DetailIntent: IIntent {
    data class LoadMovieInfo(val id: Long): DetailIntent()
}

class DetailFragment: DialogFragment(R.layout.fragment_detail), IView<DetailState> {

    private var binding: FragmentDetailBinding? = null
    private val viewModel: DetailViewModel by viewModel()
    private val genreAdapter = GenreAdapter()
    private val movieId: Long by lazy { arguments?.getLong(KEY_MOVIE_ID)?: 0 }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.run {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            setDimAmount(0.8F)
        }
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.run {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        getDialog()?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        super.onDismiss(dialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDetailBinding.bind(view)

        binding?.run {
            rvGenre.adapter = genreAdapter
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            stateDetail.setOnClickListener {
                lifecycleScope.launch {
                    if (movieId != 0L) {
                        viewModel.intents.emit(DetailIntent.LoadMovieInfo(movieId))
                    }
                }
            }
        }

        lifecycleScope.run {
            launchWhenStarted {
                if (movieId == 0L) {
                    render(DetailState.Invalidate)
                } else {
                    viewModel.intents.emit(DetailIntent.LoadMovieInfo(movieId))
                }
            }
            launchWhenResumed {
                viewModel.state.collect {
                    render(it)
                }
            }
        }
    }

    override fun render(state: DetailState) {
        when (state) {
            DetailState.Invalidate -> handleFailed(INVALIDATE)
            is DetailState.MovieState -> {
                handleLoadMovieState(state.movie)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        binding = null
    }


    private fun handleFailed(code: ErrCode) {
        binding?.run {
            tvOverviewTitle.hide()
            stateDetail.showMessage(code)
        }
    }

    private fun showLoading() {
        binding?.run {
            tvOverviewTitle.hide()
            stateDetail.showLoading()
        }
    }

    private fun handleLoadMovieState(state: Flow<State<Movie>>) {
        lifecycleScope.launch {
            state.collect {
                when (it) {
                    State.Loading -> showLoading()
                    is State.Failed -> handleFailed(it.errCode)
                    is State.Success -> {
                        showMovieInfo(it.data)
                    }
                }
            }
        }
    }

    private fun showMovieInfo(movie: Movie) {
        genreAdapter.setData(movie.genres)
        binding?.run {
            stateDetail.hide()
            tvOverviewTitle.show()
            ivThumb.load(movie.getPosterUrl())
            tvTitle.text = movie.title
            tvOverview.text = movie.overview
            tvRelease.text = "${movie.getFormatDate()} - ${movie.getFormatDuration()}"
        }
    }

    companion object {
        const val KEY_MOVIE_ID = "movieId"
    }
}