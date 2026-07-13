package vision.combat.c4.ds.sample.gallery.mapview.mapinteractor.ui

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes for the Map interactor showcase hub.
 *
 * [List] is the showcase index; [Detail] opens one [MapInteractorShowcase] by its enum
 * [Enum.name] (passed as a route argument and rehydrated with [MapInteractorShowcase.valueOf]).
 */
@Keep
@Serializable
internal sealed interface MapInteractorRoute {
    @Serializable
    data object List : MapInteractorRoute

    @Serializable
    data class Detail(val showcaseName: String) : MapInteractorRoute
}
