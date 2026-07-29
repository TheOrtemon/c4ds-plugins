package vision.combat.c4.ds.sample.gallery.network.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.network.domain.model.CurrentWeather
import vision.combat.c4.ds.sample.gallery.network.domain.model.WeatherCondition
import vision.combat.c4.ds.sample.gallery.network.ui.NetworkViewModel.Action
import vision.combat.c4.ds.sample.gallery.network.ui.NetworkViewModel.Event
import vision.combat.c4.ds.sample.gallery.network.ui.NetworkViewModel.UiState
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.button.Button
import vision.combat.c4.ds.sdk.ui.util.showToast
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel

@Composable
internal fun NetworkWindow(viewModel: NetworkViewModel = diViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WindowContent(uiState = uiState, onAction = viewModel::handleAction)
    EventHandler(eventFlow = viewModel.event)
}

@Composable
private fun WindowContent(uiState: UiState, onAction: (Action) -> Unit) {
    WindowScaffold(
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.network_tool_name)) },
        content = { Content(uiState, onAction) },
    )
}

@Composable
private fun ColumnScope.Content(uiState: UiState, onAction: (Action) -> Unit) {
    Text(
        text = stringResource(R.string.network_explainer),
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 16.dp),
    )

    Text(
        text = stringResource(R.string.network_section_position),
        style = MaterialTheme.typography.subtitle1,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 8.dp),
    )
    Card(elevation = 2.dp, modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = uiState.selectedPosition ?: stringResource(R.string.network_value_unknown),
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(12.dp),
        )
    }

    Button(
        label = stringResource(R.string.network_fetch),
        enabled = !uiState.isLoading,
        onClick = { onAction(Action.FetchWeather) },
    )

    Spacer(modifier = Modifier.height(16.dp))

    Divider(modifier = Modifier.padding(bottom = 16.dp))

    if (uiState.isLoading) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.network_fetching),
                style = MaterialTheme.typography.body2,
            )
        }
    } else {
        val weather = uiState.weather
        if (weather == null) {
            Text(
                text = stringResource(R.string.network_hint_empty),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface,
            )
        } else {
            WeatherCard(weather)
        }
    }
}

@Composable
private fun WeatherCard(weather: CurrentWeather) {
    Text(
        text = stringResource(R.string.network_section_weather),
        style = MaterialTheme.typography.subtitle1,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 8.dp),
    )
    Card(elevation = 2.dp) {
        Column(modifier = Modifier.padding(12.dp)) {
            WeatherRow(
                label = stringResource(R.string.network_label_condition),
                value = stringResource(weather.condition.labelResId()),
            )
            WeatherRow(
                label = stringResource(R.string.network_label_temperature),
                value = weather.temperatureCelsius?.let {
                    stringResource(R.string.network_temperature_value, it.toString())
                } ?: stringResource(R.string.network_value_unknown),
            )
            WeatherRow(
                label = stringResource(R.string.network_label_humidity),
                value = weather.relativeHumidityPercent?.let {
                    stringResource(R.string.network_humidity_value, it)
                } ?: stringResource(R.string.network_value_unknown),
            )
            WeatherRow(
                label = stringResource(R.string.network_label_wind),
                value = weather.windSpeedKmh?.let {
                    stringResource(R.string.network_wind_value, it.toString())
                } ?: stringResource(R.string.network_value_unknown),
            )
            WeatherRow(
                label = stringResource(R.string.network_label_observed),
                value = weather.observedAtIso ?: stringResource(R.string.network_value_unknown),
            )
        }
    }
}

@Composable
private fun WeatherRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.width(120.dp),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.body2,
        )
    }
}

private fun WeatherCondition.labelResId(): Int = when (this) {
    WeatherCondition.CLEAR -> R.string.network_condition_clear
    WeatherCondition.PARTLY_CLOUDY -> R.string.network_condition_partly_cloudy
    WeatherCondition.OVERCAST -> R.string.network_condition_overcast
    WeatherCondition.FOG -> R.string.network_condition_fog
    WeatherCondition.DRIZZLE -> R.string.network_condition_drizzle
    WeatherCondition.RAIN -> R.string.network_condition_rain
    WeatherCondition.SNOW -> R.string.network_condition_snow
    WeatherCondition.THUNDERSTORM -> R.string.network_condition_thunderstorm
    WeatherCondition.UNKNOWN -> R.string.network_condition_unknown
}

@Composable
private fun EventHandler(eventFlow: Flow<Event>) {
    val context = LocalContext.current
    val errorPrefix = stringResource(R.string.network_error_prefix)
    val unknownError = stringResource(R.string.network_error_unknown)
    LaunchedEffect(eventFlow) {
        eventFlow.collect { event ->
            when (event) {
                is Event.FetchFailed ->
                    context.showToast("$errorPrefix ${event.message ?: unknownError}")
            }
        }
    }
}
