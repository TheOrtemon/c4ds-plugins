package vision.combat.c4.ds.sample.gallery.catalog.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vision.combat.c4.ds.sample.gallery.BuildConfig
import vision.combat.c4.ds.sample.gallery.catalog.CatalogToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolManager
import vision.combat.c4.ds.sdk.tool.activate
import vision.combat.c4.ds.sdk.tool.deactivate

internal class CatalogListViewModel(
    private val toolManager: ToolManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _events = Channel<Event>(Channel.BUFFERED)
    val events: Flow<Event> = _events.receiveAsFlow()

    init {
        observeActiveTools()
    }

    fun handleAction(action: Action) {
        when (action) {
            is Action.Toggle -> toggleEntry(action.entry)
            is Action.DeactivateAll -> deactivateAll()
        }
    }

    private fun observeActiveTools() {
        viewModelScope.launch {
            toolManager.activeTools.collect { activeList ->
                val activeClassNames = activeList.map { it.id.className }.toSet()
                val canDeactivateAll = activeList.any { tool ->
                    tool.id.packageName == BuildConfig.APPLICATION_ID &&
                            tool.id.className != catalogHubClassName
                }
                _uiState.update { it.copy(activeClassNames = activeClassNames, canDeactivateAll = canDeactivateAll) }
            }
        }
    }

    private fun toggleEntry(entry: CatalogEntry) {
        if (entry.toolClassName in _uiState.value.activeClassNames) {
            toolManager.deactivate(entry.toolClassName)
        } else {
            toolManager.activate(entry.toolClassName, flags = ToolManager.FLAG_COMPONENT_ON_TOP)
        }
    }

    private fun deactivateAll() {
        toolManager.activeTools.value
            .filter { tool ->
                tool.id.packageName == BuildConfig.APPLICATION_ID &&
                        tool.id.className != catalogHubClassName
            }
            .forEach { tool -> toolManager.deactivate(tool.id) }

        viewModelScope.launch { _events.send(Event.AllDeactivated) }
    }

    data class UiState(
        val activeClassNames: Set<String> = emptySet(),
        val canDeactivateAll: Boolean = false,
    )

    sealed interface Action {
        data class Toggle(val entry: CatalogEntry) : Action
        data object DeactivateAll : Action
    }

    sealed interface Event {
        data object AllDeactivated : Event
    }

    private companion object {
        val catalogHubClassName: String = CatalogToolDescriptor::class.qualifiedName!!
    }
}
