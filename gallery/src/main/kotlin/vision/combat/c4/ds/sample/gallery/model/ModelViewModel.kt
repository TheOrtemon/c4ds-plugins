package vision.combat.c4.ds.sample.gallery.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import vision.combat.c4.ds.sdk.domain.interactor.CommonModelInteractor
import vision.combat.c4.ds.sdk.domain.interactor.selectedModelUpdatedEvent
import vision.combat.c4.ds.sdk.domain.interactor.userModelUpdatedEvent
import vision.combat.c4.model.BattlespaceConceptModel

internal class ModelViewModel(
    private val modelInteractor: CommonModelInteractor,
) : ViewModel() {

    companion object {
        private const val PREVIEW_LIMIT = 5
    }

    // isReadOnly is a StateFlow<Boolean> — read .value for snapshot
    private val _uiState = MutableStateFlow(
        UiState(isReadOnly = modelInteractor.isReadOnly.value)
    )
    val uiState: StateFlow<UiState> = _uiState

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

        // getAllModels() is a Sequence snapshot — refresh on collection change event.
        // Capped to PREVIEW_LIMIT for a compact, non-lazy list display.
        modelInteractor.modelsCollectionUpdatedEvent
            .onStart { emit(Unit) }
            .onEach {
                val list = modelInteractor.getAllModels()
                    .filterIsInstance<BattlespaceConceptModel>()
                    .take(PREVIEW_LIMIT)
                    .toList()
                _uiState.update { it.copy(allModels = list) }
            }
            .launchIn(viewModelScope)
    }

    fun selectModel(model: BattlespaceConceptModel) {
        modelInteractor.selectModel(model.id)
    }

    fun unselectModel() {
        modelInteractor.unselectModel()
    }

    // createModel / deleteModel require non-trivial args (GeoPoint, ModelAttrs, etc.)
    // and are considered advanced usage — demonstrated via observe/select/unselect here.

    data class UiState(
        val selectedModel: BattlespaceConceptModel? = null,
        val userModel: BattlespaceConceptModel? = null,
        val allModels: List<BattlespaceConceptModel> = emptyList(),
        val isReadOnly: Boolean = false,
    )
}
