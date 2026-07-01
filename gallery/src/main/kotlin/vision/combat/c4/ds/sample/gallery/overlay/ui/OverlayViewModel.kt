package vision.combat.c4.ds.sample.gallery.overlay.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import earth.worldwind.geom.Position
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import vision.combat.c4.ds.sample.gallery.overlay.OverlayToolDescriptor
import vision.combat.c4.ds.sdk.domain.interactor.CommonMapInteractor
import vision.combat.c4.ds.sdk.domain.interactor.CommonModelInteractor
import vision.combat.c4.ds.sdk.domain.interactor.settings.CommonLocaleSettingsInteractor
import vision.combat.c4.ds.sdk.domain.interactor.userModelUpdatedEvent
import vision.combat.c4.ds.sdk.domain.util.toLocation
import vision.combat.c4.ds.sdk.ui.util.toString
import vision.combat.c4.model.obj.actor.person.PersonModel
import vision.combat.c4.ds.sdk.tool.ToolManager
import vision.combat.c4.ds.sdk.tool.deactivate
import vision.combat.c4.unit.CoordinateSystemFormat

internal class OverlayViewModel(
    private val mapInteractor: CommonMapInteractor,
    private val modelInteractor: CommonModelInteractor,
    private val localeSettingsInteractor: CommonLocaleSettingsInteractor,
    private val toolManager: ToolManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())

    val uiState: StateFlow<UiState> = _uiState
        .onStart { observeData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = _uiState.value,
        )

    private fun observeData() {
        merge(modelInteractor.userModel, modelInteractor.userModelUpdatedEvent)
            .combine(localeSettingsInteractor.coordinateSystemFormat, ::updateUserPosition)
            .launchIn(viewModelScope)

        mapInteractor.selectedPosition
            .combine(localeSettingsInteractor.coordinateSystemFormat, ::updateSelectedPosition)
            .launchIn(viewModelScope)
    }

    private fun updateUserPosition(userModel: PersonModel?, coordinateSystemFormat: CoordinateSystemFormat) {
        _uiState.update {
            it.copy(
                userModel = userModel?.location?.center
                    ?.toLocation()
                    ?.toString(coordinateSystemFormat),
            )
        }
    }

    private fun updateSelectedPosition(position: Position, coordinateSystemFormat: CoordinateSystemFormat) {
        _uiState.update { it.copy(selectedPosition = position.toString(coordinateSystemFormat)) }
    }

    fun handleAction(action: Action) {
        when (action) {
            Action.Close -> toolManager.deactivate<OverlayToolDescriptor>()
        }
    }

    data class UiState(
        val selectedPosition: String? = null,
        val userModel: String? = null,
    )

    sealed interface Action {
        data object Close : Action
    }
}
