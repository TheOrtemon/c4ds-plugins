package vision.combat.c4.ds.sample.gallery.mapinteractor.ui.detail

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import earth.worldwind.geom.Position
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.domain.interactor.CommonMapInteractor
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel

/**
 * Live readout of the map's camera and lookAt state.
 *
 * The camera/lookAt are plain (non-flow) snapshots, so they are re-read on every
 * [CommonMapInteractor.mapNavigatorEvent]; [CommonMapInteractor.isLookAtAboveHorizon] and
 * [CommonMapInteractor.selectedPosition] are observed directly as StateFlows.
 */
@Composable
internal fun ColumnScope.CameraLookAtShowcase(viewModel: CameraLookAtViewModel = diViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Text(
        text = stringResource(R.string.map_sc_camera_explainer),
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.onSurface,
    )

    Divider(modifier = Modifier.padding(vertical = 8.dp))

    SectionLabel(stringResource(R.string.map_sc_camera_section))
    ReadoutRow(stringResource(R.string.map_sc_camera_lat), "%.5f°".format(state.cameraLatDeg))
    ReadoutRow(stringResource(R.string.map_sc_camera_lon), "%.5f°".format(state.cameraLonDeg))
    ReadoutRow(stringResource(R.string.map_sc_camera_alt), "%.0f m".format(state.cameraAltM))
    ReadoutRow(stringResource(R.string.map_sc_camera_heading), "%.1f°".format(state.cameraHeadingDeg))
    ReadoutRow(stringResource(R.string.map_sc_camera_tilt), "%.1f°".format(state.cameraTiltDeg))

    Divider(modifier = Modifier.padding(vertical = 8.dp))

    SectionLabel(stringResource(R.string.map_sc_lookat_section))
    ReadoutRow(stringResource(R.string.map_sc_lookat_lat), "%.5f°".format(state.lookAtLatDeg))
    ReadoutRow(stringResource(R.string.map_sc_lookat_lon), "%.5f°".format(state.lookAtLonDeg))
    ReadoutRow(stringResource(R.string.map_sc_above_horizon), state.isLookAtAboveHorizon.toString())
    ReadoutRow(
        stringResource(R.string.map_sc_selected_pos),
        "%.4f°, %.4f°".format(
            state.selectedPosition.latitude.inDegrees,
            state.selectedPosition.longitude.inDegrees,
        ),
    )
}

internal class CameraLookAtViewModel(
    private val mapInteractor: CommonMapInteractor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        UiState(selectedPosition = mapInteractor.selectedPosition.value),
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        mapInteractor.isLookAtAboveHorizon
            .onEach { above -> _uiState.update { it.copy(isLookAtAboveHorizon = above) } }
            .launchIn(viewModelScope)

        mapInteractor.selectedPosition
            .onEach { pos -> _uiState.update { it.copy(selectedPosition = pos) } }
            .launchIn(viewModelScope)

        // Re-read the (non-flow) camera / lookAt snapshots on every navigator event.
        mapInteractor.mapNavigatorEvent
            .onEach { refreshCameraState() }
            .launchIn(viewModelScope)

        refreshCameraState()
    }

    private fun refreshCameraState() {
        val camera = mapInteractor.camera
        val lookAt = mapInteractor.lookAt
        _uiState.update {
            it.copy(
                cameraLatDeg = camera.position.latitude.inDegrees,
                cameraLonDeg = camera.position.longitude.inDegrees,
                cameraAltM = camera.position.altitude,
                cameraHeadingDeg = camera.heading.inDegrees,
                cameraTiltDeg = camera.tilt.inDegrees,
                lookAtLatDeg = lookAt.position.latitude.inDegrees,
                lookAtLonDeg = lookAt.position.longitude.inDegrees,
            )
        }
    }

    data class UiState(
        val selectedPosition: Position,
        val isLookAtAboveHorizon: Boolean = false,
        val cameraLatDeg: Double = 0.0,
        val cameraLonDeg: Double = 0.0,
        val cameraAltM: Double = 0.0,
        val cameraHeadingDeg: Double = 0.0,
        val cameraTiltDeg: Double = 0.0,
        val lookAtLatDeg: Double = 0.0,
        val lookAtLonDeg: Double = 0.0,
    )
}
