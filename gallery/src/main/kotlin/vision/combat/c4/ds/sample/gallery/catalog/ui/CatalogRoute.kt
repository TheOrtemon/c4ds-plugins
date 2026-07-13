package vision.combat.c4.ds.sample.gallery.catalog.ui

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
internal sealed interface CatalogRoute {
    @Serializable
    data object List : CatalogRoute

    @Serializable
    data class CategoryDetail(val sectionName: String) : CatalogRoute

    @Serializable
    data class Detail(val entryName: String) : CatalogRoute
}
