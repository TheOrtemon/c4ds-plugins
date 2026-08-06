package vision.combat.c4.ds.example.tool.sight

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vision.combat.c4.ds.sdk.data.util.observeAsStateFlow
import vision.combat.c4.ds.sdk.domain.interactor.CommonMessengerInteractor
import vision.combat.c4.ds.sdk.domain.interactor.CommonModelInteractor
import vision.combat.c4.ds.sdk.domain.model.Channel
import vision.combat.c4.ds.sdk.domain.model.event.ModelStateEvent
import vision.combat.c4.model.BattlespaceConceptModel
import vision.combat.c4.model.location.GeoPoint
import vision.combat.c4.model.location.PointLocation
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal class SightToolViewModel(
    private val modelInteractor: CommonModelInteractor,
    private val messengerInteractor: CommonMessengerInteractor,
    private val repository: SightToolRepository,
) : ViewModel() {
    private val _channels = MutableStateFlow<List<Channel>>(emptyList())
    private val _uiState = MutableStateFlow(UiState(isPilot = repository.getIsPilotMode()))
    val uiState = _uiState.asStateFlow()
    private var hideJob: Job? = null
    private var channelListJob: Job? = null

    companion object {
        @OptIn(ExperimentalUuidApi::class)
        private val DEFAULT_ID = Uuid.parse("c7ffdc5a-0fa2-491f-97a2-f8b8db0638a6")
        private val DEFAULT_LOCATION = GeoPoint(0.0, 0.0, null)
    }

    init {
        modelInteractor.modelStateUpdatedEvent
            .onEach { checkMessages(it) }
            .launchIn(viewModelScope)

        repository.observeIsPilotMode(viewModelScope)
            .onEach { isPilot ->
                _uiState.update { it.copy(isPilot = isPilot) }
            }
            .launchIn(viewModelScope)

        updateChannelList()
    }

    fun updateLocation(x: Float, y: Float) {
        _uiState.update { currentState ->
            currentState.copy(
                xCoordinate = x,
                yCoordinate = y,
                isVisible = true
            )
        }
        startHideTimer()
    }

    private fun startHideTimer() {
        hideJob?.cancel()
        hideJob = viewModelScope.launch {
            delay(5000)
            updateVisibility(false)
        }
    }

    fun updateVisibility(isVisible: Boolean) {
        _uiState.update { it.copy(isVisible = isVisible) }
    }

    fun updatePilotMode(isPilot: Boolean) {
        _uiState.update { it.copy(isPilot = isPilot) }
        repository.setIsPilotMode(isPilot)
    }

    fun updateChannelList() {
        channelListJob?.cancel()
        channelListJob = viewModelScope.launch {
            messengerInteractor.observeChannels()
                .collect { channels ->
                    _channels.value = channels
                    _uiState.update { it.copy(channelNames = channels.map { ch -> ch.name }) }
                }
        }
    }

    fun updateSelectedChannelName(channelName: String) {
        _uiState.update { it.copy(selectedChannelName = channelName) }
    }

    fun getSelectedChannel(): Channel? {
        val name = _uiState.value.selectedChannelName ?: return null
        return _channels.value.find { it.name == name}
    }

    @OptIn(ExperimentalUuidApi::class)
    fun sendCoordinate(x: Float, y: Float) {
        val modelToSend = BattlespaceConceptModel(DEFAULT_ID, PointLocation(DEFAULT_LOCATION))
            .apply {
                name = "$x;$y"
            }
        val channel = getSelectedChannel() ?: return
        modelInteractor.consumeModel(modelToSend)
        modelInteractor.setAssignedDestinationKeys(modelToSend.id, setOf(channel.id))
        modelInteractor.commitChanges()
    }

    fun checkMessages(event: ModelStateEvent) {
        val model = event.model
        if (model is BattlespaceConceptModel) {
            val messageText = model.name ?: return
            val (x, y) = parseCoordinates(messageText) ?: return
            updateLocation(x, y)
        }
    }

    fun parseCoordinates(input: String): Pair<Float, Float>? {
        val parts = input.split(';')

        if (parts.size != 2) return null

        val x = parts[0].toFloatOrNull()
        val y = parts[1].toFloatOrNull()

        return if (x != null && y != null) {
            Pair(x, y)
        } else {
            null
        }
    }

    data class UiState (
        val xCoordinate: Float? = null,
        val yCoordinate: Float? = null,
        val isVisible: Boolean = false,
        val isPilot: Boolean = false,
        val selectedChannelName: String? = null,
        val channelNames: List<String> = emptyList(),
    )
}

internal class SightToolRepository(
    private val sharedPreferences: SharedPreferences,
) {
    fun getIsPilotMode(): Boolean {
        return sharedPreferences.getBoolean(IS_PILOT_MODE_KEY, false)
    }

    fun setIsPilotMode(isPilotMode: Boolean) {
        sharedPreferences.edit { putBoolean(IS_PILOT_MODE_KEY, isPilotMode) }
    }

    fun observeIsPilotMode(scope: CoroutineScope): StateFlow<Boolean> {
        return sharedPreferences.observeAsStateFlow<Boolean>(IS_PILOT_MODE_KEY, false, scope)
    }

    private companion object {
        private const val IS_PILOT_MODE_KEY = "is_pilot_mode"
    }
}

//DestinationChipsField(
//state = rememberDestinationChipsState(
//activeDestination = droneSettingsState.availableChannels,
//selectedDestinations = droneSettingsState.selectedChannels,
//onChange = { onAction(DroneSettingsAction.OnSelectedChannelsChanged(it)) }
//),
//horizontalPadding = 0.dp,
//)
//
//viewModelScope.launch {
//    messengerInteractor.observeChannels().collect { channels ->
//        _droneSettingsState.update { it.copy(availableChannels = channels.asAssignedChipValue()) }
//    }
//}
//
//is OnSelectedChannelsChanged -> viewModelScope.launch {
//    modelDataInteractor.modelDataHolder.value.model?.id?.let { id ->
//        val channelKeys = action.channels.filterIsInstance<DestinationChipValue.Assigned>()
//            .map { Uuid.parse(it.id) }.toSet()
//        modelInteractor.setAssignedDestinationKeys(id, channelKeys)
//    }
//    _droneSettingsState.update { it.copy(selectedChannels = action.channels) }
//}
//
//is OnSelectedChannelsChanged -> viewModelScope.launch {
//    modelDataInteractor.modelDataHolder.value.model?.id?.let { id ->
//        val channelKeys = action.channels.filterIsInstance<DestinationChipValue.Assigned>()
//            .map { Uuid.parse(it.id) }.toSet()
//        modelInteractor.setAssignedDestinationKeys(id, channelKeys)
//    }
//    _droneSettingsState.update { it.copy(selectedChannels = action.channels) }
//}
//
//
