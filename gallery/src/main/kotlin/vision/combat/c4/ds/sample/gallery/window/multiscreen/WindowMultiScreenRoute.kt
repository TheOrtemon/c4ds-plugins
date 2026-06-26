package vision.combat.c4.ds.sample.gallery.window.multiscreen

import vision.combat.c4.ds.sdk.ui.navigation.Route

internal sealed class WindowMultiScreenRoute(override val route: String) : Route {
    data object Home : WindowMultiScreenRoute("home")
    data object Settings : WindowMultiScreenRoute("settings")
}
