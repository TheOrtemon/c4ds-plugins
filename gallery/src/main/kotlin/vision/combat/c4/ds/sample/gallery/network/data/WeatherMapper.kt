package vision.combat.c4.ds.sample.gallery.network.data

import vision.combat.c4.ds.sample.gallery.network.data.body.response.OpenMeteoForecastApiModel
import vision.combat.c4.ds.sample.gallery.network.domain.model.CurrentWeather
import vision.combat.c4.ds.sample.gallery.network.domain.model.WeatherCondition

/** Maps the Open-Meteo DTO to the domain model at the data boundary. */
internal fun OpenMeteoForecastApiModel.toDomain(): CurrentWeather = CurrentWeather(
    condition = current?.weatherCode.toWeatherCondition(),
    temperatureCelsius = current?.temperatureCelsius,
    relativeHumidityPercent = current?.relativeHumidityPercent,
    windSpeedKmh = current?.windSpeedKmh,
    observedAtIso = current?.time,
)

/** Buckets the WMO weather interpretation codes Open-Meteo returns into coarse conditions. */
private fun Int?.toWeatherCondition(): WeatherCondition = when (this) {
    0 -> WeatherCondition.CLEAR
    1, 2 -> WeatherCondition.PARTLY_CLOUDY
    3 -> WeatherCondition.OVERCAST
    45, 48 -> WeatherCondition.FOG
    in 51..57 -> WeatherCondition.DRIZZLE
    in 61..67, in 80..82 -> WeatherCondition.RAIN
    in 71..77, 85, 86 -> WeatherCondition.SNOW
    in 95..99 -> WeatherCondition.THUNDERSTORM
    else -> WeatherCondition.UNKNOWN
}
