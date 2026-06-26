package vision.combat.c4.ds.sample.gallery.catalog.ui

import kotlinx.serialization.Serializable

@Serializable
internal sealed interface CatalogRoute {
    @Serializable
    data object List : CatalogRoute

    @Serializable
    data class Detail(val sampleId: String) : CatalogRoute
}
