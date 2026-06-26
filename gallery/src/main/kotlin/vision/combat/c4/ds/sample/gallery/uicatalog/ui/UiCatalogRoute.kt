package vision.combat.c4.ds.sample.gallery.uicatalog.ui

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
internal sealed interface UiCatalogRoute {
    @Serializable
    data object List : UiCatalogRoute

    @Serializable
    data class Detail(val entryName: String) : UiCatalogRoute
}
