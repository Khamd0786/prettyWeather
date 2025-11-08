package com.hk.prettyweather.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Base class for handling UI state and one-off side effects.
 *
 * @param STATE Concrete implementation of [UiState]
 * @param SIDE_EFFECT Concrete implementation of [SideEffect]
 * @param initialState State value used to initialise the flow
 * @param sideEffectDispatcher Dispatcher used when emitting side effects (defaults to [Dispatchers.Main.immediate])
 */
abstract class BaseViewModel<STATE : UiState, SIDE_EFFECT : SideEffect>(
    initialState: STATE,
    private val sideEffectDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<STATE> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<SIDE_EFFECT>()
    val sideEffect: SharedFlow<SIDE_EFFECT> = _sideEffect.asSharedFlow()

    protected val currentState: STATE
        get() = _state.value

    /**
     * Replace the state directly with a new value.
     */
    protected fun setState(newState: STATE) {
        _state.value = newState
    }

    /**
     * Update the state using the current value as input.
     */
    protected fun updateState(reducer: STATE.() -> STATE) {
        _state.update { it.reducer() }
    }

    /**
     * Emit a single use event for the UI to react to.
     */
    protected fun postSideEffect(effect: SIDE_EFFECT) {
        viewModelScope.launch(sideEffectDispatcher) {
            _sideEffect.emit(effect)
        }
    }
}

