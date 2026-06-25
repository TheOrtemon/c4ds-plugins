package vision.combat.c4.ds.sample.gallery.window.navigation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import vision.combat.c4.ds.sample.gallery.window.navigation.data.WindowNavRepository
import vision.combat.c4.ds.sdk.common.util.coroutines.mapAndStateIn

internal class SettingsViewModel(
    private val repository: WindowNavRepository,
) : ViewModel() {

    val uiState: StateFlow<UiState> = repository
        .observeOpenOnTop(viewModelScope)
        .mapAndStateIn(viewModelScope) { UiState(it) }

    fun setOpenOnTop(openOnTop: Boolean) {
        repository.setOpenOnTop(openOnTop)
    }

    data class UiState(val openOnTop: Boolean = false)
}

