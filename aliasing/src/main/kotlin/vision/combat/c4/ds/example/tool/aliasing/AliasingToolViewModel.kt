package vision.combat.c4.ds.example.tool.aliasing

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import vision.combat.c4.ds.sdk.domain.interactor.CommonModelInteractor
import vision.combat.c4.model.overlay.OverlayModel

internal class AliasingToolViewModel(
    private val modelInteractor: CommonModelInteractor
): ViewModel() {
    private val _effects = MutableSharedFlow<Effect>()
    val effects: SharedFlow<Effect> = _effects.asSharedFlow()
    var uiState by mutableStateOf(UiState())

    private fun emitEffect(effect: Effect) {
        viewModelScope.launch { _effects.emit(effect) }
    }

    private fun getValidAliasList(): List<String> {
        return if (uiState.isRandomized) {
            uiState.assetAliasList.shuffled()
        } else {
            uiState.aliasList
                .lineSequence()
                .map { it.trim() }
                .filter { it.isNotBlank() && it.length <= 100 }
                .distinct()
                .toList()
        }
    }

    fun massProcess() {
        val aliasList = getValidAliasList().ifEmpty {
            return emitEffect(Effect.NoAliases)
        }

        val selectedModel = modelInteractor.selectedModel.value ?:
            return emitEffect(Effect.ModelNotSelected)


        val layersContent = selectedModel.container
            .flatMapTo(mutableSetOf(), OverlayModel::content)

        when {
            layersContent.isEmpty() -> return emitEffect(Effect.NoLayers)
            layersContent.size > aliasList.size -> return emitEffect(Effect.NotEnoughAliases)
        }

        layersContent.zip(aliasList).forEach { (model, name) ->
            model.name = name
            modelInteractor.consumeModel(model)
        }

        modelInteractor.commitChanges()
        return emitEffect(Effect.Done)
    }

    data class UiState(
        val aliasList: String = "",
        val isRandomized: Boolean = false,
        val assetAliasList: List<String> = emptyList()
    )

    fun updateAliasList(newAliasList: String) {
        uiState = uiState.copy(aliasList = newAliasList)
    }

    fun updateIsRandomized(flag: Boolean) {
        uiState = uiState.copy(isRandomized = flag)
    }

    fun initAssets(assetList: List<String>) {
        uiState = uiState.copy(assetAliasList = assetList)
    }
}

sealed class Effect {
    data object ModelNotSelected : Effect()
    data object NoLayers : Effect()
    data object NoAliases : Effect()
    data object NotEnoughAliases : Effect()
    data object Done : Effect()
}

