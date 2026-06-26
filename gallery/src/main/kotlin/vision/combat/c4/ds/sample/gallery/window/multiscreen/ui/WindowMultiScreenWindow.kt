package vision.combat.c4.ds.sample.gallery.window.multiscreen.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import vision.combat.c4.ds.sample.gallery.window.multiscreen.WindowMultiScreenRoute
import vision.combat.c4.ds.sdk.ui.navigation.AppNavHost

@Composable
internal fun WindowMultiScreenWindow() {
    val navController = rememberNavController()

    AppNavHost(
        navController = navController,
        startDestination = WindowMultiScreenRoute.Home.route,
    ) {
        composable(WindowMultiScreenRoute.Home.route) {
            HomeScreen(navigateToSettings = { navController.navigate(WindowMultiScreenRoute.Settings.route) })
        }
        composable(WindowMultiScreenRoute.Settings.route) {
            SettingsScreen()
        }
    }
}
