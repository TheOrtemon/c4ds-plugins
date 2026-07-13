package vision.combat.c4.ds.sample.gallery.storage.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vision.combat.c4.ds.sdk.domain.interactor.CommonSessionStorageInteractor
import java.io.File

internal class StorageViewModel(
    private val storageInteractor: CommonSessionStorageInteractor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        UiState(
            rootDirectoryPath = storageInteractor.getRootDirectoryPath(),
            userDirectoryPath = storageInteractor.getUserDirectoryPath(),
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun writeFile() {
        _uiState.update { it.copy(fileOperationStatus = FileOperationStatus.InProgress) }
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching {
                    val dir = File(storageInteractor.getUserDirectoryPath())
                    dir.mkdirs()
                    val file = File(dir, SAMPLE_FILE_NAME)
                    val content = "Gallery sample — written at ${System.currentTimeMillis()}"
                    file.writeText(content)
                    content
                }
            }
            result.fold(
                onSuccess = { content ->
                    _uiState.update {
                        it.copy(
                            fileOperationStatus = FileOperationStatus.WriteSuccess,
                            lastWrittenContent = content,
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            fileOperationStatus = FileOperationStatus.Error(error.message),
                        )
                    }
                },
            )
        }
    }

    fun readFile() {
        _uiState.update { it.copy(fileOperationStatus = FileOperationStatus.InProgress) }
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching {
                    val file = File(storageInteractor.getUserDirectoryPath(), SAMPLE_FILE_NAME)
                    if (!file.exists()) null else file.readText()
                }
            }
            result.fold(
                onSuccess = { content ->
                    if (content == null) {
                        _uiState.update {
                            it.copy(fileOperationStatus = FileOperationStatus.FileNotFound)
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                fileOperationStatus = FileOperationStatus.ReadSuccess,
                                readContent = content,
                            )
                        }
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            fileOperationStatus = FileOperationStatus.Error(error.message),
                        )
                    }
                },
            )
        }
    }

    data class UiState(
        val rootDirectoryPath: String = "",
        val userDirectoryPath: String = "",
        val fileOperationStatus: FileOperationStatus = FileOperationStatus.Idle,
        val lastWrittenContent: String? = null,
        val readContent: String? = null,
    )

    sealed interface FileOperationStatus {
        data object Idle : FileOperationStatus
        data object InProgress : FileOperationStatus
        data object WriteSuccess : FileOperationStatus
        data object ReadSuccess : FileOperationStatus
        data object FileNotFound : FileOperationStatus
        /**
         * [message] is null when the underlying exception carried no detail text;
         * the UI resolves the fallback via R.string.storage_error_unknown.
         */
        data class Error(val message: String?) : FileOperationStatus
    }

    private companion object {
        const val SAMPLE_FILE_NAME = "gallery_sample.txt"
    }
}
