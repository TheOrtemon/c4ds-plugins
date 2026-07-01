package vision.combat.c4.ds.sample.gallery.mapview.mapinteractor.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import earth.worldwind.geom.Location
import earth.worldwind.geom.Sector
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.domain.interactor.CommonMapInteractor
import vision.combat.c4.ds.sdk.domain.interactor.CommonModelInteractor
import vision.combat.c4.ds.sdk.ui.component.button.Button
import vision.combat.c4.ds.sdk.ui.component.button.OutlinedButton
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel

/**
 * Moves the camera with [CommonMapInteractor.focusOnLocation] (point) and
 * [CommonMapInteractor.focusOnSector] (area). Each call is followed by
 * [CommonMapInteractor.requestRedraw] so the camera move is painted in the same frame.
 *
 * Also demonstrates focusing on the current cursor position ([CommonMapInteractor.selectedPosition])
 * and on the user's own location ([CommonModelInteractor.userModel]).
 */
@Composable
internal fun ColumnScope.FocusShowcase(viewModel: FocusViewModel = diViewModel()) {
    Text(
        text = stringResource(R.string.map_sc_focus_explainer),
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.onSurface,
    )

    Divider(modifier = Modifier.padding(vertical = 8.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            label = stringResource(R.string.map_sc_focus_location),
            onClick = viewModel::focusOnSampleLocation,
            modifier = Modifier.weight(1f),
        )
        OutlinedButton(
            label = stringResource(R.string.map_sc_focus_sector),
            onClick = viewModel::focusOnSampleSector,
            modifier = Modifier.weight(1f),
        )
    }

    Divider(modifier = Modifier.padding(vertical = 8.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            label = stringResource(R.string.map_sc_focus_cursor),
            onClick = viewModel::focusOnCursor,
            modifier = Modifier.weight(1f),
        )
        OutlinedButton(
            label = stringResource(R.string.map_sc_focus_user),
            onClick = viewModel::focusOnUser,
            modifier = Modifier.weight(1f),
        )
    }
}

internal class FocusViewModel(
    private val mapInteractor: CommonMapInteractor,
    private val modelInteractor: CommonModelInteractor,
) : ViewModel() {

    /** Center the camera on the Kyiv demo point. */
    fun focusOnSampleLocation() {
        mapInteractor.focusOnLocation(SAMPLE_LOCATION)
        // Nudge the map engine to paint the camera move in the same frame.
        mapInteractor.requestRedraw()
    }

    /** Frame a ~10° sector centered on the same Kyiv demo point. */
    fun focusOnSampleSector() {
        mapInteractor.focusOnSector(sampleSector)
        mapInteractor.requestRedraw()
    }

    /**
     * Center the camera on the current map cursor position (selectedPosition).
     * The cursor is always valid (defaults to the map center), so this is always a no-op-free action.
     */
    fun focusOnCursor() {
        val pos = mapInteractor.selectedPosition.value
        mapInteractor.focusOnLocation(
            Location.fromDegrees(pos.latitude.inDegrees, pos.longitude.inDegrees)
        )
        mapInteractor.requestRedraw()
    }

    /**
     * Center the camera on the user's own GPS location.
     * Does nothing if no user location is available yet (no GNSS fix).
     */
    fun focusOnUser() {
        val center = modelInteractor.userModel.value?.location?.center ?: return
        mapInteractor.focusOnLocation(Location.fromDegrees(center.lat, center.lon))
        mapInteractor.requestRedraw()
    }

    private companion object {
        // Kyiv, Ukraine — stable demo location (shared with MapWindow sample)
        private val SAMPLE_LOCATION = Location.fromDegrees(50.45, 30.52)
        private const val SAMPLE_SECTOR_DELTA_DEG = 10.0

        private val sampleSector: Sector
            get() {
                val halfDelta = SAMPLE_SECTOR_DELTA_DEG / 2.0
                return Sector.fromDegrees(
                    SAMPLE_LOCATION.latitude.inDegrees - halfDelta,
                    SAMPLE_LOCATION.longitude.inDegrees - halfDelta,
                    SAMPLE_SECTOR_DELTA_DEG,
                    SAMPLE_SECTOR_DELTA_DEG,
                )
            }
    }
}
