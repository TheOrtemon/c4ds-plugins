package vision.combat.c4.ds.sample.gallery.network.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import earth.worldwind.geom.Position
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vision.combat.c4.ds.sample.gallery.network.domain.WeatherInteractor
import vision.combat.c4.ds.sample.gallery.network.domain.model.CurrentWeather
import vision.combat.c4.ds.sdk.domain.interactor.settings.CommonLocaleSettingsInteractor
import vision.combat.c4.ds.sdk.ui.util.toString
import vision.combat.c4.unit.CoordinateSystemFormat

internal class NetworkViewModel(
    private val weatherInteractor: WeatherInteractor,
    private val localeSettingsInteractor: CommonLocaleSettingsInteractor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())

    val uiState: StateFlow<UiState> = _uiState
        .onStart { observeSelectedPosition() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = _uiState.value,
        )

    private val _event = Channel<Event>(Channel.BUFFERED)
    val event: Flow<Event> = _event.receiveAsFlow()

    private fun observeSelectedPosition() {
        weatherInteractor.selectedPosition
            .combine(localeSettingsInteractor.coordinateSystemFormat, ::updateSelectedPosition)
            .launchIn(viewModelScope)
    }

    private fun updateSelectedPosition(position: Position, format: CoordinateSystemFormat) {
        _uiState.update { it.copy(selectedPosition = position.toString(format)) }
    }

    fun handleAction(action: Action) {
        when (action) {
            Action.FetchWeather -> fetchWeather()
        }
    }

    private fun fetchWeather() {
        if (_uiState.value.isLoading) return
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val weather = weatherInteractor.fetchWeatherAtSelectedPosition()
                _uiState.update { it.copy(isLoading = false, weather = weather) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                _event.send(Event.FetchFailed(e.message))
            }
        }
    }

    data class UiState(
        val selectedPosition: String? = null,
        val isLoading: Boolean = false,
        val weather: CurrentWeather? = null,
    )

    sealed interface Action {
        data object FetchWeather : Action
    }

    sealed interface Event {
        data class FetchFailed(val message: String?) : Event
    }
}
