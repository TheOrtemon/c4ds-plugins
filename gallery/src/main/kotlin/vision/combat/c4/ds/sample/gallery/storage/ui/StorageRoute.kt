package vision.combat.c4.ds.sample.gallery.storage.ui

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes for the Storage showcase hub.
 *
 * [List] is the showcase index; [Detail] opens one [StorageShowcase] by its enum
 * [Enum.name] (passed as a route argument and rehydrated with [StorageShowcase.valueOf]).
 */
@Keep
@Serializable
internal sealed interface StorageRoute {
    @Serializable
    data object List : StorageRoute

    @Serializable
    data class Detail(val showcaseName: String) : StorageRoute
}
