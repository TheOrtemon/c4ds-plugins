package vision.combat.c4.ds.sample.gallery.storage.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import vision.combat.c4.ds.sample.gallery.storage.data.PreferencesStorageRepository

internal class PreferencesStorageViewModel(
    private val repository: PreferencesStorageRepository,
) : ViewModel() {

    val uiState: StateFlow<UiState> = combine(
        repository.observeString(viewModelScope),
        repository.observeCounter(viewModelScope),
    ) { str, counter ->
        UiState(savedString = str, counter = counter)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState(),
    )

    fun saveString(value: String) {
        repository.putString(value)
    }

    fun increment() {
        repository.increment()
    }

    data class UiState(
        val savedString: String = "",
        val counter: Int = 0,
    )
}
