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
import vision.combat.c4.ds.sample.gallery.toolmanagement.managed.DemoWindowTool
import vision.combat.c4.ds.sample.gallery.toolmanagement.managed.DemoWindowToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolManager
import vision.combat.c4.ds.sdk.tool.ToolManager.Companion.FLAG_COMPONENT_ON_TOP
import vision.combat.c4.ds.sdk.tool.ToolManager.Companion.FLAG_NONE
import vision.combat.c4.ds.sdk.tool.activate
import vision.combat.c4.ds.sdk.tool.deactivate
import vision.combat.c4.ds.sdk.tool.hideComponent
import vision.combat.c4.ds.sdk.tool.isActive
import vision.combat.c4.ds.sdk.tool.showComponent

internal class ToolManagementViewModel(
    private val toolManager: ToolManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        toolManager.activeTools
            .onEach { tools ->
                val isDemoToolActive = toolManager.isActive<DemoToolDescriptor>()
                val isDemoWindowToolActive = toolManager.isActive<DemoWindowToolDescriptor>()
                val activeNames = tools.map { it.id.className }
                _uiState.update {
                    it.copy(
                        isDemoToolActive = isDemoToolActive,
                        // Reset component-shown tracking to the tool's activation defaults
                        // whenever it (re)activates or fully deactivates: the overlay is
                        // `isDefault = true` so it is auto-shown on activation, the status strip
                        // is opt-in. There is no public API to observe component visibility
                        // (see DemoTool KDoc), so this sample tracks it itself.
                        isDemoOverlayShown = isDemoToolActive,
                        isDemoStatusShown = false,
                        isDemoWindowToolActive = isDemoWindowToolActive,
                        activeToolClassNames = activeNames,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Activates the hidden demo tool ([DemoToolDescriptor]). Its surfaces are a
     * [vision.combat.c4.ds.sdk.tool.ToolComponent.Overlay] and a
     * [vision.combat.c4.ds.sdk.tool.ToolComponent.Status] rendered over/around the shared map, so
     * [ToolManager.activate] showing its required and default components never evicts this window —
     * a plain `activate<DemoToolDescriptor>()` suffices, no window to fight for the front position.
     */
    fun activateDemoTool() {
        toolManager.activate<DemoToolDescriptor>()
    }

    fun deactivateDemoTool() {
        toolManager.deactivate<DemoToolDescriptor>()
    }

    /**
     * Shows [DemoToolDescriptor]'s overlay component on its own, independently of its status
     * component. Demonstrates component-level (rather than whole-tool) activation.
     *
     * There is no public API to observe component visibility from the UI layer, so this sample
     * tracks "is it currently shown" itself via [UiState.isDemoOverlayShown], flipped alongside
     * each show/hide call.
     */
    fun showDemoOverlay() {
        toolManager.showComponent<DemoToolDescriptor>(type = ToolComponent.Overlay::class)
        _uiState.update { it.copy(isDemoOverlayShown = true) }
    }

    /**
     * Hides [DemoToolDescriptor]'s overlay component. Because the overlay is declared
     * non-required, hiding it does not deactivate the demo tool — the status strip (if shown)
     * stays visible and the tool stays active.
     */
    fun hideDemoOverlay() {
        toolManager.hideComponent(ToolComponent.Overlay::class)
        _uiState.update { it.copy(isDemoOverlayShown = false) }
    }

    /**
     * Shows [DemoToolDescriptor]'s status component on its own, independently of its overlay
     * component.
     */
    fun showDemoStatus() {
        toolManager.showComponent<DemoToolDescriptor>(type = ToolComponent.Status::class)
        _uiState.update { it.copy(isDemoStatusShown = true) }
    }

    /**
     * Hides [DemoToolDescriptor]'s status component. Because the status strip is declared
     * non-required, hiding it does not deactivate the demo tool — the overlay badge (if shown)
     * stays visible and the tool stays active.
     */
    fun hideDemoStatus() {
        toolManager.hideComponent(ToolComponent.Status::class)
        _uiState.update { it.copy(isDemoStatusShown = false) }
    }

    /**
     * Activates [DemoWindowToolDescriptor] with [FLAG_COMPONENT_ON_TOP]: its window stacks ON TOP
     * of this Tool Management window instead of replacing it. This Tool Management window stays in
     * the window stack underneath, so pressing Back returns to it and it never deactivates.
     */
    fun openDemoWindowOnTop() {
        toolManager.activate<DemoWindowToolDescriptor>(
            flags = FLAG_COMPONENT_ON_TOP,
            paramsBuilder = { DemoWindowTool.PARAM_OPENED_WITH_REPLACE_FLAG to false },
        )
    }

    /**
     * Activates [DemoWindowToolDescriptor] with [FLAG_NONE]: this clears the window stack, hiding
     * this Tool Management window. Because Tool Management's window is a *required* component,
     * hiding it automatically deactivates the Tool Management tool (see
     * [vision.combat.c4.ds.sdk.tool.ToolManager.hideComponent]) — pressing Back afterwards lands on
     * the root Tools list, not back on this screen.
     */
    fun openDemoWindowReplace() {
        toolManager.activate<DemoWindowToolDescriptor>(
            flags = FLAG_NONE,
            paramsBuilder = { DemoWindowTool.PARAM_OPENED_WITH_REPLACE_FLAG to true },
        )
    }

    data class UiState(
        val isDemoToolActive: Boolean = false,
        val isDemoOverlayShown: Boolean = false,
        val isDemoStatusShown: Boolean = false,
        val isDemoWindowToolActive: Boolean = false,
        val activeToolClassNames: List<String> = emptyList(),
    )
}
