package vision.combat.c4.ds.sample.gallery.model.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.domain.interactor.CommonModelInteractor
import vision.combat.c4.ds.sdk.ui.component.reveal.DeleteMenuButton
import vision.combat.c4.ds.sdk.ui.component.reveal.EditMenuButton
import vision.combat.c4.ds.sdk.ui.component.reveal.RevealableLazyColumn
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel
import vision.combat.c4.model.BattlespaceConceptModel

/**
 * Lists every battlespace model on the map in the same visual style as the host TacticalData tool:
 * a swipe-revealable list of MIL-STD-2525 symbol rows. Swipe-start edits (selects + starts
 * interaction); swipe-end deletes (staged — committed by the host when interaction ends).
 *
 * Interactor APIs: [CommonModelInteractor.getAllModels], modelsCollectionUpdatedEvent,
 * [CommonModelInteractor.selectModel], startModelInteraction, [CommonModelInteractor.deleteModel].
 */
@Composable
internal fun ModelsListShowcase(viewModel: ModelsListViewModel = diViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    RevealableLazyColumn(
        modifier = Modifier.fillMaxSize(),
        items = state.models,
        enabledReveal = !state.isReadOnly,
        itemKey = { _, model -> model.id.toString() },
        placeholder = {
            Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.model_sc_no_models),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface,
                )
            }
        },
        startMenuItems = { _, model ->
            EditMenuButton(onClick = { viewModel.edit(model) })
        },
        endMenuItems = { _, model ->
            DeleteMenuButton(onClick = { viewModel.delete(model) })
        },
    ) { index, model ->
        ModelRow(
            model = model,
            onClick = { viewModel.select(model) },
            showDivider = index != state.models.lastIndex,
        )
    }
}

internal class ModelsListViewModel(
    private val modelInteractor: CommonModelInteractor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState(isReadOnly = modelInteractor.isReadOnly.value))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        modelInteractor.isReadOnly
            .onEach { ro -> _uiState.update { it.copy(isReadOnly = ro) } }
            .launchIn(viewModelScope)

        // Re-snapshot the (lazy Sequence) collection whenever models are added/removed.
        modelInteractor.modelsCollectionUpdatedEvent
            .onStart { emit(Unit) }
            .onEach {
                val list = modelInteractor.getAllModels()
                    .filterIsInstance<BattlespaceConceptModel>()
                    .toList()
                _uiState.update { it.copy(models = list) }
            }
            .launchIn(viewModelScope)
    }

    fun select(model: BattlespaceConceptModel) {
        modelInteractor.selectModel(model.id)
    }

    fun edit(model: BattlespaceConceptModel) {
        modelInteractor.selectModel(model.id)
        modelInteractor.startModelInteraction(model)
    }

    fun delete(model: BattlespaceConceptModel) {
        modelInteractor.deleteModel(model)
    }

    data class UiState(
        val models: List<BattlespaceConceptModel> = emptyList(),
        val isReadOnly: Boolean = false,
    )
}
