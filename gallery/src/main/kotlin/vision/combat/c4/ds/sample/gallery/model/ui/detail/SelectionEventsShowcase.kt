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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.domain.interactor.CommonModelInteractor
import vision.combat.c4.ds.sdk.domain.interactor.selectedModelUpdatedEvent
import vision.combat.c4.ds.sdk.domain.interactor.userModelUpdatedEvent
import vision.combat.c4.ds.sdk.ui.component.button.OutlinedButton
import vision.combat.c4.ds.sdk.ui.component.button.TextButton
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel
import vision.combat.c4.model.BattlespaceConceptModel

/**
 * Shows how to observe interactor state/events and drive selection & interaction:
 * selectedModel (+ selectedModelUpdatedEvent), userModel (+ userModelUpdatedEvent), the in-memory
 * collection size (modelsCollectionUpdatedEvent), isReadOnly; plus unselect / follow / switch mode.
 */
@Composable
internal fun ColumnScope.SelectionEventsShowcase(viewModel: SelectionEventsViewModel = diViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.isReadOnly) {
        Text(
            text = stringResource(R.string.model_sc_read_only),
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.error,
        )
    }

    val none = stringResource(R.string.model_sc_none)
    Readout(stringResource(R.string.model_sc_selected_label), state.selectedModel?.name ?: none)
    Readout(stringResource(R.string.model_sc_user_label), state.userModel?.name ?: none)
    Readout(stringResource(R.string.model_sc_count_label), state.modelCount.toString())

    state.selectedModel?.let { selected ->
        Divider(modifier = Modifier.padding(vertical = 4.dp))
        ModelRow(model = selected)
        OutlinedButton(
            label = stringResource(R.string.model_sc_follow),
            onClick = viewModel::followSelected,
        )
        TextButton(
            label = stringResource(R.string.model_sc_unselect),
            onClick = viewModel::unselect,
        )
    }
}

internal class SelectionEventsViewModel(
    private val modelInteractor: CommonModelInteractor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState(isReadOnly = modelInteractor.isReadOnly.value))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        modelInteractor.isReadOnly
            .onEach { ro -> _uiState.update { it.copy(isReadOnly = ro) } }
            .launchIn(viewModelScope)

        merge(modelInteractor.selectedModel, modelInteractor.selectedModelUpdatedEvent)
            .onEach { m -> _uiState.update { it.copy(selectedModel = m) } }
            .launchIn(viewModelScope)

        merge(modelInteractor.userModel, modelInteractor.userModelUpdatedEvent)
            .onEach { m -> _uiState.update { it.copy(userModel = m) } }
            .launchIn(viewModelScope)

        modelInteractor.modelsCollectionUpdatedEvent
            .onStart { emit(Unit) }
            .onEach {
                val count = modelInteractor.getAllModels().filterIsInstance<BattlespaceConceptModel>().count()
                _uiState.update { it.copy(modelCount = count) }
            }
            .launchIn(viewModelScope)
    }

    fun unselect() {
        modelInteractor.unselectModel()
    }

    fun followSelected() {
        _uiState.value.selectedModel?.let { modelInteractor.followModel(it) }
    }

    data class UiState(
        val selectedModel: BattlespaceConceptModel? = null,
        val userModel: BattlespaceConceptModel? = null,
        val modelCount: Int = 0,
        val isReadOnly: Boolean = false,
    )
}
