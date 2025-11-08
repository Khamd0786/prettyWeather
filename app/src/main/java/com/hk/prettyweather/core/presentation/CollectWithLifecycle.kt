package com.hk.prettyweather.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> Flow<T>.collectWithLifecycleChanges(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    collector: suspend (T) -> Unit,
) {
    val flow = remember(this) { this }
    LaunchedEffect(flow, lifecycleOwner, minActiveState) {
        lifecycleOwner.repeatOnLifecycle(minActiveState) {
            flow.collect(collector)
        }
    }
}

