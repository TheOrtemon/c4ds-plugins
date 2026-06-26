package vision.combat.c4.ds.sample.gallery.window.multiscreen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vision.combat.c4.ds.sample.gallery.window.multiscreen.WindowMultiScreenInteractor

internal class SettingsViewModel(
    private val interactor: WindowMultiScreenInteractor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())

    val uiState: StateFlow<UiState> = _uiState
        .onStart { observeShowDescription() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = _uiState.value,
        )

    private val _events = Channel<Event>(Channel.BUFFERED)
    val events: Flow<Event> = _events.receiveAsFlow()

    init { /* intentionally empty — work starts on first collection */ }

    fun handleAction(action: Action) {
        when (action) {
            is Action.SetShowDescription -> {
                interactor.setShowDescription(action.show)
                emitEvent { Event.SettingSaved }
            }
        }
    }

    private fun observeShowDescription() {
        interactor.observeShowDescription(viewModelScope)
            .onEach { show -> _uiState.update { it.copy(showDescription = show) } }
            .launchIn(viewModelScope)
    }

    private fun emitEvent(producer: () -> Event) {
        viewModelScope.launch { _events.send(producer()) }
    }

    data class UiState(val showDescription: Boolean = true)

    sealed interface Action {
        data class SetShowDescription(val show: Boolean) : Action
    }

    sealed interface Event {
        data object SettingSaved : Event
    }
}
