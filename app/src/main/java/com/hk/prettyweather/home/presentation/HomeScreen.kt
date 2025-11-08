package com.hk.prettyweather.home.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hk.prettyweather.core.presentation.collectWithLifecycleChanges
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    viewModel.sideEffect.collectWithLifecycleChanges { sideEffect ->
        when (sideEffect) {
            is HomeSideEffect.ShowMessage ->
                scope.launch { snackbarHostState.showSnackbar(sideEffect.message) }
        }
    }

    HomeScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onCitySelected = viewModel::onCitySelected,
        onRefresh = viewModel::onManualRefresh,
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    state: HomeState,
    snackbarHostState: SnackbarHostState,
    onCitySelected: (String) -> Unit,
    onRefresh: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val selectedIndex = remember(state.cities, state.selectedCityId) {
        state.cities.indexOfFirst { it.id == state.selectedCityId }.takeIf { it >= 0 } ?: 0
    }
    val lastUpdated = remember(state.lastUpdatedEpochSeconds) {
        state.lastUpdatedEpochSeconds?.let {
            Instant.ofEpochSecond(it)
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("MMM d, yyyy - HH:mm:ss"))
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            if (state.isRefreshing || state.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            }
            if (state.cities.isNotEmpty()) {
                TabRow(selectedTabIndex = selectedIndex) {
                    state.cities.forEachIndexed { index, city ->
                        Tab(
                            selected = index == selectedIndex,
                            onClick = { onCitySelected(city.id) },
                            text = { Text(text = city.name) },
                        )
                    }
                }
            }

            WeatherSummary(
                modifier = Modifier.fillMaxWidth(),
                title = state.weather?.cityName
                    ?: state.cities.getOrNull(selectedIndex)?.name
                    ?: "Weather",
                temperature = state.weather?.temperature,
                condition = state.weather?.condition,
                humidity = state.weather?.humidity,
                windSpeed = state.weather?.windSpeed,
                isLoading = state.isLoading,
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Button(
                    onClick = onRefresh,
                    enabled = !state.isRefreshing && !state.isLoading,
                ) {
                    Text(text = if (state.isRefreshing) "Refreshing..." else "Refresh")
                }
                if (lastUpdated != null) {
                    Text(
                        text = "Last updated: $lastUpdated",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                if (state.error != null) {
                    Text(
                        text = state.error,
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
private fun WeatherSummary(
    modifier: Modifier = Modifier,
    title: String,
    temperature: Double?,
    condition: String?,
    humidity: Int?,
    windSpeed: Double?,
    isLoading: Boolean,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Text(
                    text = temperature?.let { "${String.format(Locale.getDefault(), "%.1f", it)}°C" } ?: "--",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = condition ?: "-",
                    style = MaterialTheme.typography.titleMedium,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Humidity",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                        )
                        Text(
                            text = humidity?.let { "$it%" } ?: "--",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Wind",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                        )
                        Text(
                            text = windSpeed?.let { "${String.format(Locale.getDefault(), "%.1f", it)} m/s" } ?: "--",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        }
    }
}

