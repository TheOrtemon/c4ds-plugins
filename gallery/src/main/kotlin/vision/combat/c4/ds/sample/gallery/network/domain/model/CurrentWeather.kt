package vision.combat.c4.ds.sample.gallery.network.domain.model

/** Domain model of the weather at a position — exactly what the UI renders, nothing more. */
internal data class CurrentWeather(
    val condition: WeatherCondition,
    val temperatureCelsius: Double?,
    val relativeHumidityPercent: Int?,
    val windSpeedKmh: Double?,
    val observedAtIso: String?,
)

/** Coarse weather buckets; mapping from raw WMO codes happens in the data layer. */
internal enum class WeatherCondition {
    CLEAR,
    PARTLY_CLOUDY,
    OVERCAST,
    FOG,
    DRIZZLE,
    RAIN,
    SNOW,
    THUNDERSTORM,
    UNKNOWN,
}
