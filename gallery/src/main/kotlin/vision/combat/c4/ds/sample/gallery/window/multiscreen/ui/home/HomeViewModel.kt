package vision.combat.c4.ds.sample.gallery.window.multiscreen.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vision.combat.c4.ds.sample.gallery.window.multiscreen.domain.WindowMultiScreenInteractor

internal class HomeViewModel(
    private val interactor: WindowMultiScreenInteractor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _events = Channel<Event>(Channel.BUFFERED)
    val events: Flow<Event> = _events.receiveAsFlow()

    init {
        observeShowDescription()
    }

    fun handleAction(action: Action) {
        when (action) {
            Action.OpenSettings -> sendEvent { Event.NavigateToSettings }
        }
    }

    private fun observeShowDescription() {
        interactor.observeShowDescription(viewModelScope)
            .onEach { show -> _uiState.update { it.copy(showDescription = show) } }
            .launchIn(viewModelScope)
    }

    private fun sendEvent(producer: () -> Event) {
        viewModelScope.launch { _events.send(producer()) }
    }

    data class UiState(val showDescription: Boolean = true)

    sealed interface Action {
        data object OpenSettings : Action
    }

    sealed interface Event {
        data object NavigateToSettings : Event
    }
}
