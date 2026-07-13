package vision.combat.c4.ds.sample.gallery.mapoverlays.overlayisdefault.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import vision.combat.c4.ds.sample.gallery.mapoverlays.overlayisdefault.managed.DemoOverlayADescriptor
import vision.combat.c4.ds.sample.gallery.mapoverlays.overlayisdefault.managed.DemoOverlayBDescriptor
import vision.combat.c4.ds.sdk.tool.ToolManager
import vision.combat.c4.ds.sdk.tool.activate
import vision.combat.c4.ds.sdk.tool.deactivate
import vision.combat.c4.ds.sdk.tool.isActive

internal class OverlayDefaultViewModel(
    private val toolManager: ToolManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        toolManager.activeTools
            .onEach {
                _uiState.update {
                    it.copy(
                        isDemoAActive = toolManager.isActive<DemoOverlayADescriptor>(),
                        isDemoBActive = toolManager.isActive<DemoOverlayBDescriptor>(),
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Activates the hidden [DemoOverlayADescriptor] tool. Its overlay is `isDefault = true`, so
     * [ToolManager.activate] auto-shows it — a plain `activate<DemoOverlayADescriptor>()` suffices,
     * no `showComponent` call needed.
     */
    fun activateDemoA() {
        toolManager.activate<DemoOverlayADescriptor>()
    }

    /**
     * Deactivates the hidden [DemoOverlayADescriptor] tool, removing its default overlay entirely
     * — if Demo B is currently displacing it, there is no Demo A overlay left to auto-restore once
     * Demo B is later deactivated.
     */
    fun deactivateDemoA() {
        toolManager.deactivate<DemoOverlayADescriptor>()
    }

    /**
     * Activates the hidden [DemoOverlayBDescriptor] tool. Its overlay is also `isDefault = true`,
     * so activating it auto-shows this overlay, which displaces Demo A's overlay in the same
     * region — no manual `showComponent`/`hideComponent` orchestration needed.
     */
    fun activateDemoB() {
        toolManager.activate<DemoOverlayBDescriptor>()
    }

    /**
     * Deactivates the hidden [DemoOverlayBDescriptor] tool. Because Demo A's overlay is also a
     * default component, hiding the overlay that displaced it automatically restores it — per the
     * SDK's documented default-component replace/restore contract.
     */
    fun deactivateDemoB() {
        toolManager.deactivate<DemoOverlayBDescriptor>()
    }

    data class UiState(
        val isDemoAActive: Boolean = false,
        val isDemoBActive: Boolean = false,
    )
}
