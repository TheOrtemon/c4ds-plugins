package vision.combat.c4.ds.sample.gallery.mapinteractor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import earth.worldwind.geom.Location
import earth.worldwind.geom.Position
import earth.worldwind.geom.Sector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import vision.combat.c4.ds.sdk.domain.interactor.CommonMapInteractor
import vision.combat.c4.ds.sdk.domain.model.MapDisplayMode

internal class MapInteractorViewModel(
    private val mapInteractor: CommonMapInteractor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        UiState(selectedPosition = mapInteractor.selectedPosition.value),
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        mapInteractor.isLookAtAboveHorizon
            .onEach { above -> _uiState.update { it.copy(isLookAtAboveHorizon = above) } }
            .launchIn(viewModelScope)

        mapInteractor.mapDisplayMode
            .onEach { mode -> _uiState.update { it.copy(displayMode = mode) } }
            .launchIn(viewModelScope)

        mapInteractor.arDistanceLimit
            .onEach { dist -> _uiState.update { it.copy(arDistanceLimit = dist) } }
            .launchIn(viewModelScope)

        mapInteractor.isReticleVisible
            .onEach { v -> _uiState.update { it.copy(isReticleVisible = v) } }
            .launchIn(viewModelScope)

        mapInteractor.isCursorPinned
            .onEach { p -> _uiState.update { it.copy(isCursorPinned = p) } }
            .launchIn(viewModelScope)

        mapInteractor.isMapVisible
            .onEach { v -> _uiState.update { it.copy(isMapVisible = v) } }
            .launchIn(viewModelScope)

        mapInteractor.selectedPosition
            .onEach { pos -> _uiState.update { it.copy(selectedPosition = pos) } }
            .launchIn(viewModelScope)

        // Update camera / corrections on every navigator event
        mapInteractor.mapNavigatorEvent
            .onEach { refreshCameraState() }
            .launchIn(viewModelScope)

        refreshCameraState()
    }

    private fun refreshCameraState() {
        val camera = mapInteractor.camera
        val lookAt = mapInteractor.lookAt
        _uiState.update {
            it.copy(
                cameraLatDeg = camera.position.latitude.inDegrees,
                cameraLonDeg = camera.position.longitude.inDegrees,
                cameraAltM = camera.position.altitude,
                cameraHeadingDeg = camera.heading.inDegrees,
                cameraTiltDeg = camera.tilt.inDegrees,
                lookAtLatDeg = lookAt.position.latitude.inDegrees,
                lookAtLonDeg = lookAt.position.longitude.inDegrees,
                declination = mapInteractor.getDeclination(),
                convergence = mapInteractor.getConvergence(),
                angleCorrection = mapInteractor.getAngleCorrection(),
            )
        }
    }

    fun setReticleVisible(visible: Boolean) {
        mapInteractor.setReticleVisible(visible)
    }

    fun pinCursor() {
        mapInteractor.pinCursor()
    }

    fun unpinCursor() {
        mapInteractor.unpinCursor()
    }

    fun setDisplayMode(mode: MapDisplayMode) {
        mapInteractor.updateMapDisplayMode { mode }
    }

    fun setArDistanceLimit(distanceM: Double) {
        mapInteractor.setArDistanceLimit(distanceM)
    }

    fun setMapVisible(visible: Boolean) {
        mapInteractor.setMapVisible(visible)
    }

    fun focusOnSampleLocation() {
        mapInteractor.focusOnLocation(SAMPLE_LOCATION)
        // Nudge the map engine to paint the camera move in the same frame.
        mapInteractor.requestRedraw()
    }

    fun focusOnSampleSector() {
        // ~10° sector centered on the same Kyiv demo point as focusOnSampleLocation
        mapInteractor.focusOnSector(sampleSector)
        mapInteractor.requestRedraw()
    }

    override fun onCleared() {
        mapInteractor.updateMapDisplayMode { MapDisplayMode.Normal }
        mapInteractor.setMapVisible(true)
        mapInteractor.setReticleVisible(false)
        super.onCleared()
    }

    data class UiState(
        val displayMode: MapDisplayMode = MapDisplayMode.Normal,
        val arDistanceLimit: Double = 3000.0,
        val isReticleVisible: Boolean = false,
        val isCursorPinned: Boolean = false,
        val isLookAtAboveHorizon: Boolean = false,
        val isMapVisible: Boolean = true,
        val selectedPosition: Position,
        val cameraLatDeg: Double = 0.0,
        val cameraLonDeg: Double = 0.0,
        val cameraAltM: Double = 0.0,
        val cameraHeadingDeg: Double = 0.0,
        val cameraTiltDeg: Double = 0.0,
        val lookAtLatDeg: Double = 0.0,
        val lookAtLonDeg: Double = 0.0,
        val declination: Float = 0f,
        val convergence: Float = 0f,
        val angleCorrection: Float = 0f,
    )

    companion object {
        // Kyiv, Ukraine — stable demo location (shared with MapWindow sample)
        private val SAMPLE_LOCATION = Location.fromDegrees(50.45, 30.52)
        private const val SAMPLE_SECTOR_DELTA_DEG = 10.0

        private val sampleSector: Sector
            get() {
                val halfDelta = SAMPLE_SECTOR_DELTA_DEG / 2.0
                return Sector.fromDegrees(
                    SAMPLE_LOCATION.latitude.inDegrees - halfDelta,
                    SAMPLE_LOCATION.longitude.inDegrees - halfDelta,
                    SAMPLE_SECTOR_DELTA_DEG,
                    SAMPLE_SECTOR_DELTA_DEG,
                )
            }
    }
}
