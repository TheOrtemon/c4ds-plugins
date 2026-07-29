package vision.combat.c4.ds.sample.gallery.network.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import vision.combat.c4.ds.sample.gallery.network.data.body.response.OpenMeteoForecastApiModel

/**
 * Thin Ktor service around the keyless Open-Meteo forecast endpoint. Pure transport: builds
 * the request and lets ContentNegotiation deserialize the response; error mapping is the
 * caller's job. [clientProvider] postpones the heavy `HttpClient` creation until the first
 * request — the same shape as the SDK's own `GoogleMapsApiService`.
 */
internal class WeatherApiService(
    private val clientProvider: () -> HttpClient,
) {
    private inline val client get() = clientProvider()

    suspend fun getCurrentWeather(latitude: Double, longitude: Double): OpenMeteoForecastApiModel =
        client.get(FORECAST_URL) {
            url {
                parameters.append("latitude", latitude.toString())
                parameters.append("longitude", longitude.toString())
                parameters.append("current", CURRENT_FIELDS)
            }
        }.body()

    private companion object {
        private const val FORECAST_URL = "https://api.open-meteo.com/v1/forecast"
        private const val CURRENT_FIELDS =
            "temperature_2m,relative_humidity_2m,wind_speed_10m,weather_code"
    }
}
