package vision.combat.c4.ds.sample.gallery.mapinteractor.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.mapinteractor.MapInteractorViewModel
import vision.combat.c4.ds.sdk.domain.model.MapDisplayMode
import vision.combat.c4.ds.sdk.ui.component.IntegerStepper
import vision.combat.c4.ds.sdk.ui.component.SegmentedButtonItem
import vision.combat.c4.ds.sdk.ui.component.SegmentedButtonRow
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.button.Button
import vision.combat.c4.ds.sdk.ui.component.button.OutlinedButton
import vision.combat.c4.ds.sdk.ui.component.checkable.SwitchField
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel

@Composable
internal fun MapInteractorWindow(viewModel: MapInteractorViewModel = diViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WindowScaffold(
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.map_interactor_tool_name)) },
        content = { MapInteractorContent(uiState, viewModel) },
    )
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.subtitle1,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
    )
}

@Composable
private fun ReadoutRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface,
        )
    }
}

@Suppress("LongMethod")
@Composable
private fun MapInteractorContent(
    uiState: MapInteractorViewModel.UiState,
    viewModel: MapInteractorViewModel,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {

        // ── Camera / LookAt readout ──────────────────────────────────────────
        SectionLabel(stringResource(R.string.map_interactor_section_camera))
        ReadoutRow(stringResource(R.string.map_interactor_camera_lat), "%.5f°".format(uiState.cameraLatDeg))
        ReadoutRow(stringResource(R.string.map_interactor_camera_lon), "%.5f°".format(uiState.cameraLonDeg))
        ReadoutRow(stringResource(R.string.map_interactor_camera_alt), "%.0f m".format(uiState.cameraAltM))
        ReadoutRow(stringResource(R.string.map_interactor_camera_heading), "%.1f°".format(uiState.cameraHeadingDeg))
        ReadoutRow(stringResource(R.string.map_interactor_camera_tilt), "%.1f°".format(uiState.cameraTiltDeg))
        ReadoutRow(stringResource(R.string.map_interactor_lookat_lat), "%.5f°".format(uiState.lookAtLatDeg))
        ReadoutRow(stringResource(R.string.map_interactor_lookat_lon), "%.5f°".format(uiState.lookAtLonDeg))
        ReadoutRow(
            stringResource(R.string.map_interactor_above_horizon),
            uiState.isLookAtAboveHorizon.toString(),
        )

        ReadoutRow(
            stringResource(R.string.map_interactor_selected_pos),
            "%.4f°, %.4f°".format(
                uiState.selectedPosition.latitude.inDegrees,
                uiState.selectedPosition.longitude.inDegrees,
            ),
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // ── Display mode ─────────────────────────────────────────────────────
        SectionLabel(stringResource(R.string.map_interactor_section_display_mode))
        val displayModes = listOf(
            SegmentedButtonItem(MapDisplayMode.Normal, stringResource(R.string.map_interactor_mode_normal)),
            SegmentedButtonItem(MapDisplayMode.AR, stringResource(R.string.map_interactor_mode_ar)),
            SegmentedButtonItem(MapDisplayMode.VR, stringResource(R.string.map_interactor_mode_vr)),
        )
        SegmentedButtonRow(
            items = displayModes,
            selected = uiState.displayMode,
            onSelected = { viewModel.setDisplayMode(it) },
            modifier = Modifier.fillMaxWidth(),
        )
        if (uiState.displayMode == MapDisplayMode.AR) {
            Spacer(modifier = Modifier.height(4.dp))
            IntegerStepper(
                value = uiState.arDistanceLimit.toInt(),
                label = stringResource(R.string.map_interactor_ar_distance),
                valueRange = 500..20000,
                smallStep = 500,
                largeStep = 2000,
                onValueChange = { viewModel.setArDistanceLimit(it.toDouble()) },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // ── Reticle & cursor pin ─────────────────────────────────────────────
        SectionLabel(stringResource(R.string.map_interactor_section_controls))
        SwitchField(
            initialValue = uiState.isReticleVisible,
            label = stringResource(R.string.map_interactor_reticle),
            onCheckedChange = { viewModel.setReticleVisible(it) },
        )
        SwitchField(
            initialValue = uiState.isMapVisible,
            label = stringResource(R.string.map_interactor_map_visible),
            onCheckedChange = { viewModel.setMapVisible(it) },
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                label = stringResource(R.string.map_interactor_pin_cursor),
                onClick = { viewModel.pinCursor() },
                enabled = !uiState.isCursorPinned,
                modifier = Modifier.weight(1f),
            )
            OutlinedButton(
                label = stringResource(R.string.map_interactor_unpin_cursor),
                onClick = { viewModel.unpinCursor() },
                enabled = uiState.isCursorPinned,
                modifier = Modifier.weight(1f),
            )
        }
        ReadoutRow(stringResource(R.string.map_interactor_cursor_pinned), uiState.isCursorPinned.toString())

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // ── Focus actions ────────────────────────────────────────────────────
        SectionLabel(stringResource(R.string.map_interactor_section_focus))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                label = stringResource(R.string.map_interactor_focus_location),
                onClick = { viewModel.focusOnSampleLocation() },
                modifier = Modifier.weight(1f),
            )
            OutlinedButton(
                label = stringResource(R.string.map_interactor_focus_sector),
                onClick = { viewModel.focusOnSampleSector() },
                modifier = Modifier.weight(1f),
            )
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // ── Corrections ──────────────────────────────────────────────────────
        SectionLabel(stringResource(R.string.map_interactor_section_corrections))
        ReadoutRow(
            stringResource(R.string.map_interactor_declination),
            "%.2f°".format(uiState.declination),
        )
        ReadoutRow(
            stringResource(R.string.map_interactor_convergence),
            "%.2f°".format(uiState.convergence),
        )
        ReadoutRow(
            stringResource(R.string.map_interactor_angle_correction),
            "%.2f°".format(uiState.angleCorrection),
        )
    }
}
