package vision.combat.c4.ds.sample.gallery.network.data.body.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response body of the Open-Meteo `/v1/forecast` endpoint — only the fields the sample
 * renders. The client's `Json { ignoreUnknownKeys = true }` drops everything else.
 */
@Serializable
internal data class OpenMeteoForecastApiModel(
    @SerialName("current") val current: Current? = null,
) {
    @Serializable
    internal data class Current(
        @SerialName("time") val time: String? = null,
        @SerialName("temperature_2m") val temperatureCelsius: Double? = null,
        @SerialName("relative_humidity_2m") val relativeHumidityPercent: Int? = null,
        @SerialName("wind_speed_10m") val windSpeedKmh: Double? = null,
        @SerialName("weather_code") val weatherCode: Int? = null,
    )
}
