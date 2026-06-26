package vision.combat.c4.ds.sample.gallery.uicatalog.ui

import kotlinx.serialization.Serializable

@Serializable
internal sealed interface UiCatalogRoute {
    @Serializable
    data object List : UiCatalogRoute

    @Serializable
    data class Detail(val componentId: String) : UiCatalogRoute
}
