package vision.combat.c4.ds.sample.gallery.window.navigation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import vision.combat.c4.ds.sample.gallery.window.navigation.data.WindowNavRepository
import vision.combat.c4.ds.sdk.tool.ToolId

internal class HomeViewModel(
    private val repository: WindowNavRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _event = MutableSharedFlow<Event>()
    val event = _event.asSharedFlow()

    init {
        repository.observeOpenOnTop(viewModelScope)
            .onEach { _uiState.value = UiState(openOnTop = it) }
            .launchIn(viewModelScope)
    }

    fun handleAction(action: Action) {
        when (action) {
            is Action.ActivateTool -> emitEvent(Event.ToolActivationRequested(action.toolId, action.onTop))
        }
    }

    private fun emitEvent(event: Event) {
        viewModelScope.launch { _event.emit(event) }
    }

    data class UiState(val openOnTop: Boolean = false)

    sealed interface Action {
        data class ActivateTool(val toolId: ToolId, val onTop: Boolean) : Action
    }

    sealed interface Event {
        data class ToolActivationRequested(val toolId: ToolId, val onTop: Boolean) : Event
    }
}

