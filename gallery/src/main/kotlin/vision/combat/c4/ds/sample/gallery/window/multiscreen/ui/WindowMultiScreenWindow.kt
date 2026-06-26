package vision.combat.c4.ds.sample.gallery.window.multiscreen.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import vision.combat.c4.ds.sample.gallery.window.multiscreen.ui.home.HomeScreen
import vision.combat.c4.ds.sample.gallery.window.multiscreen.ui.settings.SettingsScreen
import vision.combat.c4.ds.sdk.ui.navigation.AppNavHost

@Composable
internal fun WindowMultiScreenWindow() {
    val navController = rememberNavController()

    AppNavHost(
        navController = navController,
        startDestination = WindowMultiScreenRoute.Home,
    ) {
        composable<WindowMultiScreenRoute.Home> {
            HomeScreen(navigateToSettings = { navController.navigate(WindowMultiScreenRoute.Settings) })
        }
        composable<WindowMultiScreenRoute.Settings> {
            SettingsScreen()
        }
    }
}
