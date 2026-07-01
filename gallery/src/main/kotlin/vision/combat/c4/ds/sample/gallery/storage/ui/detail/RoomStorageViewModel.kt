package vision.combat.c4.ds.sample.gallery.storage.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vision.combat.c4.ds.sample.gallery.storage.data.db.dao.NoteDao
import vision.combat.c4.ds.sample.gallery.storage.data.db.entity.NoteEntity

internal class RoomStorageViewModel(
    private val noteDao: NoteDao,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadNotes()
    }

    fun addNote(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                noteDao.insert(NoteEntity(text = text.trim()))
            }
            loadNotes()
        }
    }

    fun clearNotes() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                noteDao.clearAll()
            }
            loadNotes()
        }
    }

    private fun loadNotes() {
        viewModelScope.launch {
            val notes = withContext(Dispatchers.IO) { noteDao.getAll() }
            _uiState.update { it.copy(notes = notes) }
        }
    }

    data class UiState(
        val notes: List<NoteEntity> = emptyList(),
    )
}
