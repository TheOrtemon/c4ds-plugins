package vision.combat.c4.ds.sample.gallery.window.singlescreen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class WindowSingleScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())

    val uiState: StateFlow<UiState> = _uiState
        .onStart { /* no-op: state is ready immediately */ }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = _uiState.value,
        )

    private val _event = Channel<Event>(Channel.BUFFERED)
    val event: Flow<Event> = _event.receiveAsFlow()

    init { /* intentionally empty — work starts on first collection */ }

    fun handleAction(action: Action) {
        when (action) {
            is Action.Increment -> _uiState.update { it.copy(count = it.count + 1) }
            is Action.Reset -> {
                _uiState.update { it.copy(count = 0) }
                emitEvent(Event.CounterReset)
            }
        }
    }

    private fun emitEvent(event: Event) {
        viewModelScope.launch { _event.send(event) }
    }

    data class UiState(val count: Int = 0)

    sealed interface Action {
        data object Increment : Action
        data object Reset : Action
    }

    sealed interface Event {
        data object CounterReset : Event
    }
}
