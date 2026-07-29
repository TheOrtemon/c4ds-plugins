package vision.combat.c4.ds.sample.gallery.network.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import vision.combat.c4.ds.sample.gallery.network.domain.model.CurrentWeather
import vision.combat.c4.ds.sample.gallery.network.domain.repository.WeatherRepository

/**
 * Ktor-backed [WeatherRepository]. Runs the request off the main thread and maps the DTO to
 * the domain model at the boundary; transport failures propagate to the caller, which turns
 * them into UI state.
 */
internal class WeatherRepositoryImpl(
    private val weatherApiService: WeatherApiService,
) : WeatherRepository {

    override suspend fun fetchCurrentWeather(latitude: Double, longitude: Double): CurrentWeather =
        withContext(Dispatchers.IO) {
            weatherApiService.getCurrentWeather(latitude, longitude).toDomain()
        }
}
