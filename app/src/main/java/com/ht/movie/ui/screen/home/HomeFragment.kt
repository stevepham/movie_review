package com.ht.movie.ui.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.ht.movie.adapter.MoviesAdapter
import com.ht.movie.adapter.PosterAdapter
import com.ht.movie.ext.hide
import com.ht.movie.ui.screen.base.IIntent
import com.ht.movie.ui.screen.base.IState
import com.ht.movie.ui.screen.base.IView
import com.ht.movie.ui.screen.detail.DetailFragment
import com.ht117.app.R
import com.ht117.app.databinding.FragmentHomeBinding
import com.ht117.data.model.EMPTY
import com.ht117.data.model.Movie
import com.ht117.data.model.State
import com.ht117.data.model.UNKNOWN
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

typealias IMovieCallback = (Long) -> Unit

sealed class HomeState: IState {
    object Initialize: HomeState()
    data class Data(val posters: Flow<State<List<Movie>>>,
                    val popular: Flow<PagingData<Movie>>): HomeState()
}

sealed class HomeIntent: IIntent {
    object Init: HomeIntent()
}

class HomeFragment: Fragment(R.layout.fragment_home), IView<HomeState> {

    private var binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModel()
    private val posterAdapter = PosterAdapter(this::selectMovie)
    private val popularAdapter = MoviesAdapter(this::selectMovie)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)

        binding?.run {
            rvPoster.adapter = posterAdapter
            rvPopular.adapter = popularAdapter
            popularState.setOnClickListener {
                lifecycleScope.launch {
                    popularAdapter.retry()
                }
            }
            posterState.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.intents.emit(HomeIntent.Init)
                }
            }
        }

        lifecycleScope.run {
            launchWhenCreated {
                flowOf(viewModel.state, popularAdapter.loadStateFlow).flattenMerge()
                    .collect {
                        if (it is HomeState) {
                            render(it)
                        } else if (it is CombinedLoadStates) {
                            if (it.refresh is LoadState.Loading && popularAdapter.itemCount == 0) {
                                binding?.popularState?.showLoading()
                            } else if (it.refresh is LoadState.Error && popularAdapter.itemCount == 0) {
                                binding?.popularState?.showMessage(UNKNOWN)
                            } else if (it.refresh is LoadState.NotLoading && popularAdapter.itemCount == 0) {
                                binding?.popularState?.showMessage(EMPTY)
                            } else {
                                binding?.popularState?.hide()
                            }
                        }
                    }
            }
            launchWhenResumed { viewModel.intents.emit(HomeIntent.Init) }
        }
    }

    override fun render(state: HomeState) {
        when (state) {
            is HomeState.Data -> {
                handlePoster(state.posters)
                handlePopular(state.popular)
            }
            else -> {}
        }
    }

    private fun handlePoster(poster: Flow<State<List<Movie>>>) {
        lifecycleScope.launch {
            poster.collect {
                when (it) {
                    State.Loading -> {
                        binding?.posterState?.showLoading()
                    }
                    is State.Success -> {
                        binding?.posterState?.hide()
                        posterAdapter.addItems(it.data)
                    }
                    is State.Failed -> {
                        binding?.posterState?.showMessage(it.errCode)
                    }
                    else -> { }
                }
            }
        }
    }

    private fun handlePopular(popular: Flow<PagingData<Movie>>) {
        lifecycleScope.launch {
            popular.collectLatest { paging ->
                popularAdapter.submitData(paging)
            }
        }
    }

    private fun selectMovie(id: Long) {
        val bundle = bundleOf(DetailFragment.KEY_MOVIE_ID to id)
        findNavController().navigate(R.id.selectMovieDetail, bundle)
    }
}