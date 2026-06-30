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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.domain.interactor.CommonMapInteractor
import vision.combat.c4.ds.sdk.domain.util.toGeoPoint
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel

/**
 * Magnetic / grid corrections and GeoHelper geodesy at the current lookAt.
 *
 *  - getDeclination / getConvergence / getAngleCorrection report the magnetic & grid corrections.
 *  - getDistanceAndAngle(camera, selected) returns the [distanceMeters, azimuthDeg] between the
 *    camera and the selected position.
 *  - getElevation(lat, lon) reports terrain elevation under the lookAt.
 *
 * All values are re-read on every [CommonMapInteractor.mapNavigatorEvent].
 */
@Composable
internal fun ColumnScope.CorrectionsShowcase(viewModel: CorrectionsViewModel = diViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Text(
        text = stringResource(R.string.map_sc_corrections_explainer),
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.onSurface,
    )

    Divider(modifier = Modifier.padding(vertical = 8.dp))

    SectionLabel(stringResource(R.string.map_sc_corrections_section))
    ReadoutRow(stringResource(R.string.map_sc_declination), "%.2f°".format(state.declination))
    ReadoutRow(stringResource(R.string.map_sc_convergence), "%.2f°".format(state.convergence))
    ReadoutRow(stringResource(R.string.map_sc_angle_correction), "%.2f°".format(state.angleCorrection))

    Divider(modifier = Modifier.padding(vertical = 8.dp))

    SectionLabel(stringResource(R.string.map_sc_geodesy_section))
    ReadoutRow(stringResource(R.string.map_sc_distance), "%.0f m".format(state.distanceM))
    ReadoutRow(stringResource(R.string.map_sc_azimuth), "%.1f°".format(state.azimuthDeg))
    ReadoutRow(stringResource(R.string.map_sc_elevation), "%.0f m".format(state.terrainElevationM))
}

internal class CorrectionsViewModel(
    private val mapInteractor: CommonMapInteractor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        // Corrections & geodesy depend on the (non-flow) camera / lookAt, so refresh on each event.
        mapInteractor.mapNavigatorEvent
            .onEach { refresh() }
            .launchIn(viewModelScope)

        refresh()
    }

    private fun refresh() {
        val camera = mapInteractor.camera
        val lookAt = mapInteractor.lookAt
        val selected = mapInteractor.selectedPosition.value
        val (distance, azimuth) = mapInteractor.getDistanceAndAngle(
            camera.position.toGeoPoint(withAltitude = true),
            selected.toGeoPoint(withAltitude = true),
        )
        _uiState.update {
            it.copy(
                declination = mapInteractor.getDeclination(),
                convergence = mapInteractor.getConvergence(),
                angleCorrection = mapInteractor.getAngleCorrection(),
                distanceM = distance,
                azimuthDeg = azimuth,
                terrainElevationM = mapInteractor.getElevation(
                    lookAt.position.latitude.inDegrees,
                    lookAt.position.longitude.inDegrees,
                ),
            )
        }
    }

    data class UiState(
        val declination: Float = 0f,
        val convergence: Float = 0f,
        val angleCorrection: Float = 0f,
        val distanceM: Double = 0.0,
        val azimuthDeg: Double = 0.0,
        val terrainElevationM: Double = 0.0,
    )
}
