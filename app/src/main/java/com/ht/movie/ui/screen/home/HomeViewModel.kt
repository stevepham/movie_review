package com.ht.movie.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ht.movie.ui.screen.base.IModel
import com.ht117.data.repo.IMovieRepo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(private val movieRepo: IMovieRepo): ViewModel(), IModel<HomeState, HomeIntent> {

    override val intents = MutableSharedFlow<HomeIntent>()
    private val _state = MutableStateFlow<HomeState>(HomeState.Initialize)
    override val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            intents.collect {
                when (it) {
                    HomeIntent.Init -> {
                        loadMovieAndPoster()
                    }
                }
            }
        }
    }

    private fun loadMovieAndPoster() = viewModelScope.launch {
        _state.value = HomeState.Data(posters = movieRepo.getNowPlaying(),
            popular = movieRepo.getPopular().cachedIn(viewModelScope))
    }
}