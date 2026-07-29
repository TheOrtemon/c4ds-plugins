package vision.combat.c4.ds.sample.gallery.network.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.bindSingletonOf
import org.kodein.di.instance
import org.kodein.di.provider
import vision.combat.c4.ds.sample.gallery.network.data.WeatherApiService
import vision.combat.c4.ds.sample.gallery.network.data.WeatherRepositoryImpl
import vision.combat.c4.ds.sample.gallery.network.domain.WeatherInteractor
import vision.combat.c4.ds.sample.gallery.network.domain.repository.WeatherRepository

internal val networkModule = DI.Module("networkModule") {
    // The tool's own Ktor client. The SDK deliberately leaves the untagged HttpClient slot
    // free (its shared client is bound under SdkRemoteTags.HTTP_CLIENT), so a tool can bind
    // a client configured for its own API without a Kodein OverridingException. The Android
    // engine and all Ktor/serialization classes are host-provided via compileOnly(c4ds-sdk).
    bindSingleton {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }

    // HttpClient creation is heavy (Ktor uses reflection internally), so the service takes a
    // provider and resolves the client lazily on the first request — the same shape as the
    // SDK's own GoogleMapsApiService.
    bindSingleton { WeatherApiService(provider()) }

    bindSingleton<WeatherRepository> { WeatherRepositoryImpl(instance()) }

    bindSingletonOf(::WeatherInteractor)
}
