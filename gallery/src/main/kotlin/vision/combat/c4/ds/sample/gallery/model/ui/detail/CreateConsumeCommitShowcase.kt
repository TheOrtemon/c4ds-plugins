package vision.combat.c4.ds.sample.gallery.model.ui.detail

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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.domain.interactor.CommonMapInteractor
import vision.combat.c4.ds.sdk.domain.interactor.CommonModelInteractor
import vision.combat.c4.ds.sdk.domain.model.Hostility
import vision.combat.c4.ds.sdk.domain.model.ModelAttrs
import vision.combat.c4.ds.sdk.domain.util.toGeoPoint
import vision.combat.c4.ds.sdk.ui.component.button.Button
import vision.combat.c4.ds.sdk.ui.component.button.OutlinedButton
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel
import vision.combat.c4.model.BattlespaceConceptModel

/**
 * Demonstrates the create -> consume -> commit lifecycle and the difference between consume and commit.
 *
 *  - createModel(...) builds a model but does NOT show or persist it.
 *  - consumeModel(...) visualizes it on the map and stages it as a pending LOCAL change
 *    (not written to the database, not broadcast to other nodes). Reversible via rollbackChanges().
 *  - commitModel(...) persists the staged model to the database AND broadcasts it to remote nodes.
 */
@Composable
internal fun ColumnScope.CreateConsumeCommitShowcase(
    viewModel: CreateConsumeCommitViewModel = diViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Text(
        text = stringResource(R.string.model_sc_create_explainer),
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.onSurface,
    )

    Divider(modifier = Modifier.padding(vertical = 8.dp))

    Text(
        text = stringResource(R.string.model_sc_create_status, stringResource(state.stage.labelResId)),
        style = MaterialTheme.typography.subtitle2,
        color = MaterialTheme.colors.onSurface,
    )
    state.detail?.let { detail ->
        Text(text = detail, style = MaterialTheme.typography.caption, color = MaterialTheme.colors.onSurface)
    }

    Button(
        label = stringResource(R.string.model_sc_create_btn),
        onClick = viewModel::createAndConsume,
    )
    OutlinedButton(
        label = stringResource(R.string.model_sc_commit_btn),
        onClick = viewModel::commit,
        enabled = state.stage == CreateConsumeCommitViewModel.Stage.CONSUMED,
    )
    OutlinedButton(
        label = stringResource(R.string.model_sc_rollback_btn),
        onClick = viewModel::rollback,
        enabled = state.stage == CreateConsumeCommitViewModel.Stage.CONSUMED,
    )
}

internal class CreateConsumeCommitViewModel(
    private val modelInteractor: CommonModelInteractor,
    private val mapInteractor: CommonMapInteractor,
) : ViewModel() {

    enum class Stage(val labelResId: Int) {
        IDLE(R.string.model_sc_stage_idle),
        CONSUMED(R.string.model_sc_stage_consumed),
        COMMITTED(R.string.model_sc_stage_committed),
        ROLLED_BACK(R.string.model_sc_stage_rolledback),
        FAILED(R.string.model_sc_stage_failed),
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var created: BattlespaceConceptModel? = null
    private var counter = 0

    /** Build a friendly model at the current selected position and visualize it (staged, local). */
    fun createAndConsume() {
        val startPoint = mapInteractor.selectedPosition.value.toGeoPoint(withAltitude = true)
        val attrs = ModelAttrs(
            symbolKey = RIFLE_SYMBOL_KEY,
            hostility = Hostility.Friend.name,
            name = "Sample model ${++counter}",
        )
        // A point symbol needs no real pixel shift; identity shifter keeps the start point.
        val model = modelInteractor.createModel(startPoint, attrs, { point, _, _ -> point }, templated = true)
        if (model == null) {
            _uiState.update { it.copy(stage = Stage.FAILED, detail = null) }
            return
        }
        modelInteractor.consumeModel(model, attachToDefault = true)
        mapInteractor.requestRedraw()
        created = model
        _uiState.update { it.copy(stage = Stage.CONSUMED, detail = model.name) }
    }

    /** Persist the staged model to the database and broadcast it to remote nodes. */
    fun commit() {
        val model = created ?: return
        viewModelScope.launch {
            modelInteractor.commitModel(model)
            _uiState.update { it.copy(stage = Stage.COMMITTED, detail = model.name) }
        }
    }

    /** Discard all staged (uncommitted) local changes since the last commit. */
    fun rollback() {
        modelInteractor.rollbackChanges()
        mapInteractor.requestRedraw()
        created = null
        _uiState.update { it.copy(stage = Stage.ROLLED_BACK, detail = null) }
    }

    data class UiState(
        val stage: Stage = Stage.IDLE,
        val detail: String? = null,
    )

    private companion object {
        // MIL-STD-2525 "Semiautomatic Rifle" — a simple single-point friendly symbol.
        private const val RIFLE_SYMBOL_KEY = 1160
    }
}
