package vision.combat.c4.ds.sample.gallery.network.domain.repository

import vision.combat.c4.ds.sample.gallery.network.domain.model.CurrentWeather

/**
 * Domain-facing contract for fetching weather. The Ktor transport hides behind it — nothing
 * above the data layer knows HTTP is involved. Transport/deserialization failures propagate
 * as exceptions for the caller to map.
 */
internal interface WeatherRepository {
    suspend fun fetchCurrentWeather(latitude: Double, longitude: Double): CurrentWeather
}
