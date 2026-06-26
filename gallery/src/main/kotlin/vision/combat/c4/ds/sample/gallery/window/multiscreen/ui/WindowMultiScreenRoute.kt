package vision.combat.c4.ds.sample.gallery.window.multiscreen.ui

import kotlinx.serialization.Serializable

@Serializable
internal sealed interface WindowMultiScreenRoute {
    @Serializable
    data object Home : WindowMultiScreenRoute
    @Serializable
    data object Settings : WindowMultiScreenRoute
}
