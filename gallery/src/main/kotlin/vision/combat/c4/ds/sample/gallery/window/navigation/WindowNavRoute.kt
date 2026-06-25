package vision.combat.c4.ds.sample.gallery.window.navigation

import vision.combat.c4.ds.sdk.ui.navigation.Route

internal sealed class WindowNavRoute(override val route: String) : Route {
    data object Home : WindowNavRoute("home")
    data object Settings : WindowNavRoute("settings")
}

