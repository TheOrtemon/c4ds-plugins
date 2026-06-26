@file:Suppress("UnusedReceiverParameter")

package vision.combat.c4.ds.sample.gallery.window.simple

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.window.simple.WindowSimpleViewModel.Action
import vision.combat.c4.ds.sample.gallery.window.simple.WindowSimpleViewModel.Event
import vision.combat.c4.ds.sample.gallery.window.simple.WindowSimpleViewModel.UiState
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.button.TextButton
import vision.combat.c4.ds.sdk.ui.component.coordinates.CoordinatesInputWithSystem
import vision.combat.c4.ds.sdk.ui.component.measurement.AltitudeInput
import vision.combat.c4.ds.sdk.ui.component.measurement.AngleInput
import vision.combat.c4.ds.sdk.ui.component.measurement.DistanceInput
import vision.combat.c4.ds.sdk.ui.component.measurement.SpeedInput
import vision.combat.c4.ds.sdk.ui.util.showToast
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel

@Composable
internal fun WindowSimpleWindow(viewModel: WindowSimpleViewModel = diViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WindowContent(uiState = uiState, onAction = viewModel::handleAction)
    EventHandler(eventFlow = viewModel.event)
}

@Composable
private fun WindowContent(uiState: UiState, onAction: (Action) -> Unit) {
    WindowScaffold(
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.window_simple_tool_name)) },
        content = { Content(uiState, onAction) },
    )
}

@Composable
private fun ColumnScope.Content(uiState: UiState, onAction: (Action) -> Unit) {
    Text(
        text = stringResource(R.string.window_simple_user_model),
        style = MaterialTheme.typography.h6,
        modifier = Modifier.padding(bottom = 8.dp),
    )
    ModelInfo(info = uiState.userModel)

    Divider(modifier = Modifier.padding(vertical = 16.dp))

    Text(
        text = stringResource(R.string.window_simple_selected_model),
        style = MaterialTheme.typography.h6,
        modifier = Modifier.padding(bottom = 8.dp),
    )
    ModelInfo(info = uiState.selectedModel)
    TextButton(
        label = stringResource(R.string.window_simple_unselect),
        onClick = { onAction(Action.ClearSelection) },
        enabled = uiState.selectedModel != null,
    )

    Divider(modifier = Modifier.padding(vertical = 16.dp))

    Text(
        text = stringResource(R.string.window_simple_section_inputs),
        style = MaterialTheme.typography.h6,
        modifier = Modifier.padding(bottom = 8.dp),
    )
    MeasurementInputsDemo()
}

@Composable
private fun ColumnScope.ModelInfo(info: UiState.ModelInfo?) {
    if (info != null) {
        OutlinedTextField(
            value = info.name ?: stringResource(R.string.window_simple_value_not_specified),
            enabled = false,
            onValueChange = {},
            label = { Text(stringResource(R.string.window_simple_model_name)) },
            modifier = Modifier.fillMaxWidth(),
        )
        CoordinatesInputWithSystem(
            location = info.location,
            onLocationChanged = {},
            enabled = false,
        )
    } else {
        Text(stringResource(R.string.window_simple_not_selected))
    }
}

@Composable
private fun ColumnScope.MeasurementInputsDemo() {
    var distance by remember { mutableStateOf<Double?>(1500.0) }
    var speed by remember { mutableStateOf<Float?>(15f) }
    var altitude by remember { mutableStateOf<Double?>(120.0) }
    var azimuth by remember { mutableStateOf<Double?>(90.0) }

    DistanceInput(
        distanceMetres = distance,
        onValueChange = { distance = it },
        label = stringResource(R.string.window_simple_distance),
        modifier = Modifier.fillMaxWidth(),
    )
    SpeedInput(
        speedMps = speed,
        onValueChange = { speed = it },
        label = stringResource(R.string.window_simple_speed),
        modifier = Modifier.fillMaxWidth(),
    )
    AltitudeInput(
        altitudeMetres = altitude,
        onValueChange = { altitude = it },
        label = stringResource(R.string.window_simple_altitude),
        modifier = Modifier.fillMaxWidth(),
    )
    AngleInput(
        angleDegrees = azimuth,
        onValueChange = { azimuth = it },
        label = stringResource(R.string.window_simple_azimuth),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun EventHandler(eventFlow: Flow<Event>) {
    val context = LocalContext.current
    val unselectedMessage = stringResource(R.string.window_simple_unselected_toast)
    LaunchedEffect(eventFlow) {
        eventFlow.collect { event ->
            when (event) {
                is Event.ModelUnselected -> context.showToast(unselectedMessage)
            }
        }
    }
}

