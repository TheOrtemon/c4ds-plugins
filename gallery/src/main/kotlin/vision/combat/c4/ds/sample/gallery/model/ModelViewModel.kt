package vision.combat.c4.ds.sample.gallery.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vision.combat.c4.ds.sdk.domain.interactor.CommonModelInteractor
import vision.combat.c4.ds.sdk.domain.interactor.selectedModelUpdatedEvent
import vision.combat.c4.ds.sdk.domain.interactor.userModelUpdatedEvent
import vision.combat.c4.model.BattlespaceConceptModel
import vision.combat.c4.model.ModelId

internal class ModelViewModel(
    private val modelInteractor: CommonModelInteractor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState(isReadOnly = modelInteractor.isReadOnly))
    val uiState: StateFlow<UiState> = _uiState

    private val _event = MutableSharedFlow<Event>()
    val event = _event.asSharedFlow()

    init {
        merge(modelInteractor.selectedModel, modelInteractor.selectedModelUpdatedEvent)
            .onEach { m -> _uiState.update { it.copy(selectedModel = m) } }
            .launchIn(viewModelScope)

        merge(modelInteractor.userModel, modelInteractor.userModelUpdatedEvent)
            .onEach { m -> _uiState.update { it.copy(userModel = m) } }
            .launchIn(viewModelScope)

        modelInteractor.getAllModels()
            .onEach { list -> _uiState.update { it.copy(allModels = list) } }
            .launchIn(viewModelScope)
    }

    fun selectModel(id: ModelId) {
        modelInteractor.selectModel(id)
    }

    fun unselectModel() {
        modelInteractor.unselectModel()
    }

    fun createModel() {
        if (modelInteractor.isReadOnly) return
        viewModelScope.launch {
            runCatching { modelInteractor.createModel() }
                .onFailure { emitEvent(Event.Error(it.message ?: "create failed")) }
        }
    }

    fun deleteModel(id: ModelId) {
        if (modelInteractor.isReadOnly) return
        viewModelScope.launch {
            runCatching { modelInteractor.deleteModel(id) }
                .onFailure { emitEvent(Event.Error(it.message ?: "delete failed")) }
        }
    }

    private fun emitEvent(event: Event) {
        viewModelScope.launch { _event.emit(event) }
    }

    data class UiState(
        val selectedModel: BattlespaceConceptModel? = null,
        val userModel: BattlespaceConceptModel? = null,
        val allModels: List<BattlespaceConceptModel> = emptyList(),
        val isReadOnly: Boolean = false,
    )

    sealed interface Event {
        data class Error(val message: String) : Event
    }
}

