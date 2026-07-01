package vision.combat.c4.ds.sample.gallery.panelstate.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import vision.combat.c4.ds.sdk.ui.manager.PanelManager
import vision.combat.c4.ds.sdk.ui.manager.PanelState

internal class PanelStateViewModel(
    private val panelManager: PanelManager,
) : ViewModel() {

    val panelState: StateFlow<PanelState> = panelManager.panelState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = panelManager.panelState.value,
        )

    fun openHalf() {
        panelManager.openPanel(PanelState.Opened.Half)
    }

    fun openFull() {
        panelManager.openPanel(PanelState.Opened.Full())
    }

    fun close() {
        panelManager.closePanel()
    }
}
