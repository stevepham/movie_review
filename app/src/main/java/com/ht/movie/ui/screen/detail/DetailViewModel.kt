package com.ht.movie.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ht.movie.ui.screen.base.IModel
import com.ht117.data.repo.IMovieRepo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailViewModel(private val movieRepo: IMovieRepo): ViewModel(), IModel<DetailState, DetailIntent> {

    override val intents = MutableSharedFlow<DetailIntent>()

    private val _state = MutableStateFlow<DetailState>(DetailState.Initialize)
    override val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            intents.collect {
                when (it) {
                    is DetailIntent.LoadMovieInfo -> {
                        loadMovieInfo(it.id)
                    }
                }
            }
        }
    }

    private fun loadMovieInfo(id: Long) = viewModelScope.launch {
        _state.value = DetailState.MovieState(movieRepo.getDetail(id))
    }
}