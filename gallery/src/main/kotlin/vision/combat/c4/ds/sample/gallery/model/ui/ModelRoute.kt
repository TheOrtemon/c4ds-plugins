package vision.combat.c4.ds.sample.gallery.model.ui

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes for the Model interactor showcase hub.
 *
 * [List] is the showcase index; [Detail] opens one [ModelShowcase] by its enum
 * [Enum.name] (passed as a route argument and rehydrated with [ModelShowcase.valueOf]).
 */
@Keep
@Serializable
internal sealed interface ModelRoute {
    @Serializable
    data object List : ModelRoute

    @Serializable
    data class Detail(val showcaseName: String) : ModelRoute
}
