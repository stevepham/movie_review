package com.ht.movie.ui.screen.base

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IIntent

interface IState

interface IModel<State: IState, Intent: IIntent> {
    val intents: SharedFlow<Intent>
    val state: StateFlow<State>
}

interface IView<State: IState> {
    fun render(state: State)
}