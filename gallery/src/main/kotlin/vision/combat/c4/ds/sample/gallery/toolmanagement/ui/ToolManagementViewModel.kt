package vision.combat.c4.ds.sample.gallery.toolmanagement.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import vision.combat.c4.ds.sample.gallery.toolmanagement.managed.DemoToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolManager
import vision.combat.c4.ds.sdk.tool.activate
import vision.combat.c4.ds.sdk.tool.deactivate
import vision.combat.c4.ds.sdk.tool.isActive

internal class ToolManagementViewModel(
    private val toolManager: ToolManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        toolManager.activeTools
            .onEach { tools ->
                val isDemoToolActive = toolManager.isActive<DemoToolDescriptor>()
                val activeNames = tools.map { it.id.className }
                _uiState.update { it.copy(isDemoToolActive = isDemoToolActive, activeToolClassNames = activeNames) }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Activates the hidden demo tool ([DemoToolDescriptor]). Its only surface is a
     * [vision.combat.c4.ds.sdk.tool.ToolComponent.Overlay] rendered over the shared map, so
     * [ToolManager.activate] showing its required components never evicts this window — a plain
     * `activate<DemoToolDescriptor>()` suffices, no window to fight for the front position.
     */
    fun activateDemoTool() {
        toolManager.activate<DemoToolDescriptor>()
    }

    fun deactivateDemoTool() {
        toolManager.deactivate<DemoToolDescriptor>()
    }

    data class UiState(
        val isDemoToolActive: Boolean = false,
        val activeToolClassNames: List<String> = emptyList(),
    )
}
