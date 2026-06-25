package vision.combat.c4.ds.sample.gallery.overlay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import earth.worldwind.geom.Position
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.update
import vision.combat.c4.ds.sdk.domain.interactor.CommonMapInteractor
import vision.combat.c4.ds.sdk.domain.interactor.CommonModelInteractor
import vision.combat.c4.ds.sdk.domain.interactor.settings.CommonLocaleSettingsInteractor
import vision.combat.c4.ds.sdk.domain.interactor.userModelUpdatedEvent
import vision.combat.c4.ds.sdk.domain.util.toLocation
import vision.combat.c4.ds.sdk.ui.util.toString
import vision.combat.c4.model.obj.actor.person.PersonModel
import vision.combat.c4.unit.CoordinateSystemFormat

internal class OverlaySampleViewModel(
    mapInteractor: CommonMapInteractor,
    modelInteractor: CommonModelInteractor,
    localeSettingsInteractor: CommonLocaleSettingsInteractor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
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

    data class UiState(
        val selectedPosition: String? = null,
        val userModel: String? = null,
    )
}

