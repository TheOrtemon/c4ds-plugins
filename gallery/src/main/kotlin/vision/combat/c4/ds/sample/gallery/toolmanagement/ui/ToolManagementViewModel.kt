package vision.combat.c4.ds.sample.gallery.toolmanagement.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import vision.combat.c4.ds.sample.gallery.mapview.map.MapToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolComponent
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
                val isMapActive = toolManager.isActive<MapToolDescriptor>()
                val activeNames = tools.map { it.id.className }
                _uiState.update { it.copy(isMapToolActive = isMapActive, activeToolClassNames = activeNames) }
            }
            .launchIn(viewModelScope)
    }

    fun activateMapTool() {
        toolManager.activate<MapToolDescriptor>(flags = ToolManager.FLAG_COMPONENT_ON_TOP)
    }

    fun deactivateMapTool() {
        toolManager.deactivate<MapToolDescriptor>()
    }

    fun showMapWindow() {
        val mapTool = toolManager.activeTools.value
            .firstOrNull { it.descriptor is MapToolDescriptor }
        if (mapTool != null) {
            val window = mapTool.window
            if (window != null) {
                toolManager.showComponent(window, ToolManager.FLAG_COMPONENT_ON_TOP)
            }
        }
    }

    data class UiState(
        val isMapToolActive: Boolean = false,
        val activeToolClassNames: List<String> = emptyList(),
    )
}
