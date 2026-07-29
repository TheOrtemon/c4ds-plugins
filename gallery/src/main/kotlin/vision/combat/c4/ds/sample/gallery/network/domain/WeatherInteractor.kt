package vision.combat.c4.ds.sample.gallery.network.domain

import earth.worldwind.geom.Position
import kotlinx.coroutines.flow.StateFlow
import vision.combat.c4.ds.sample.gallery.network.domain.model.CurrentWeather
import vision.combat.c4.ds.sample.gallery.network.domain.repository.WeatherRepository
import vision.combat.c4.ds.sdk.domain.interactor.CommonMapInteractor

/**
 * Combines the tool's own repository with the SDK's [CommonMapInteractor]: the weather is
 * always fetched for the position currently selected on the map.
 */
internal class WeatherInteractor(
    private val repository: WeatherRepository,
    private val mapInteractor: CommonMapInteractor,
) {
    val selectedPosition: StateFlow<Position> = mapInteractor.selectedPosition

    suspend fun fetchWeatherAtSelectedPosition(): CurrentWeather {
        val position = selectedPosition.value
        return repository.fetchCurrentWeather(
            latitude = position.latitude.inDegrees,
            longitude = position.longitude.inDegrees,
        )
    }
}
