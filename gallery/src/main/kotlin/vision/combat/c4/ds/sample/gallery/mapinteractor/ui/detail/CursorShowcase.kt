package vision.combat.c4.ds.sample.gallery.mapinteractor.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
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
import vision.combat.c4.ds.sdk.ui.component.button.Button
import vision.combat.c4.ds.sdk.ui.component.button.OutlinedButton
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel

/**
 * Pins / unpins the map cursor with [CommonMapInteractor.pinCursor] and
 * [CommonMapInteractor.unpinCursor]. The buttons are gated on
 * [CommonMapInteractor.isCursorPinned], with that state and the current
 * [CommonMapInteractor.selectedPosition] shown as live readouts.
 */
@Composable
internal fun ColumnScope.CursorShowcase(viewModel: CursorViewModel = diViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Text(
        text = stringResource(R.string.map_sc_cursor_explainer),
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.onSurface,
    )

    Divider(modifier = Modifier.padding(vertical = 8.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            label = stringResource(R.string.map_sc_pin_cursor),
            onClick = viewModel::pinCursor,
            enabled = !state.isCursorPinned,
            modifier = Modifier.weight(1f),
        )
        OutlinedButton(
            label = stringResource(R.string.map_sc_unpin_cursor),
            onClick = viewModel::unpinCursor,
            enabled = state.isCursorPinned,
            modifier = Modifier.weight(1f),
        )
    }

    Divider(modifier = Modifier.padding(vertical = 8.dp))

    ReadoutRow(stringResource(R.string.map_sc_cursor_pinned), state.isCursorPinned.toString())
    ReadoutRow(
        stringResource(R.string.map_sc_selected_pos),
        "%.4f°, %.4f°".format(
            state.selectedPosition.latitude.inDegrees,
            state.selectedPosition.longitude.inDegrees,
        ),
    )
}

internal class CursorViewModel(
    private val mapInteractor: CommonMapInteractor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        UiState(selectedPosition = mapInteractor.selectedPosition.value),
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        mapInteractor.isCursorPinned
            .onEach { pinned -> _uiState.update { it.copy(isCursorPinned = pinned) } }
            .launchIn(viewModelScope)

        mapInteractor.selectedPosition
            .onEach { pos -> _uiState.update { it.copy(selectedPosition = pos) } }
            .launchIn(viewModelScope)
    }

    fun pinCursor() {
        mapInteractor.pinCursor()
    }

    fun unpinCursor() {
        mapInteractor.unpinCursor()
    }

    data class UiState(
        val selectedPosition: Position,
        val isCursorPinned: Boolean = false,
    )
}
