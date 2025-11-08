package com.hk.prettyweather.core.presentation

/**
 * Marker interface for feature state models.
 *
 * Every UI state should expose loading and error information so the UI can react consistently.
 */
interface UiState {
    val isLoading: Boolean
    val error: String?
}

