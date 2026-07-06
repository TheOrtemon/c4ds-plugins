package vision.combat.c4.ds.sample.bookmarks.ui

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
import vision.combat.c4.ds.sample.bookmarks.domain.interactor.BookmarkInteractor
import vision.combat.c4.ds.sample.bookmarks.domain.model.Bookmark

internal class BookmarksViewModel(
    private val interactor: BookmarkInteractor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _event = Channel<Event>(Channel.BUFFERED)
    val event: Flow<Event> = _event.receiveAsFlow()

    init {
        observeBookmarks()
    }

    fun handleAction(action: Action) {
        when (action) {
            is Action.AddBookmark -> addBookmark(action.label, action.target)
            is Action.ClearBookmarks -> interactor.clearBookmarks()
        }
    }

    private fun observeBookmarks() {
        interactor.observeBookmarks(viewModelScope)
            .onEach { bookmarks -> _uiState.update { it.copy(bookmarks = bookmarks) } }
            .launchIn(viewModelScope)
    }

    private fun addBookmark(label: String, target: String) {
        interactor.addBookmark(label, target)
        emitEvent(Event.BookmarkAdded)
    }

    private fun emitEvent(event: Event) {
        viewModelScope.launch { _event.send(event) }
    }

    data class UiState(val bookmarks: List<Bookmark> = emptyList())

    sealed interface Action {
        data class AddBookmark(val label: String, val target: String) : Action
        data object ClearBookmarks : Action
    }

    sealed interface Event {
        data object BookmarkAdded : Event
    }
}
