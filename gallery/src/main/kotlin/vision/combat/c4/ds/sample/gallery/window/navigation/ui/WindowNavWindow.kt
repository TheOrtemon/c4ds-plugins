package vision.combat.c4.ds.sample.gallery.window.navigation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import vision.combat.c4.ds.sample.gallery.window.navigation.WindowNavRoute
import vision.combat.c4.ds.sdk.ui.navigation.AppNavHost

@Composable
internal fun WindowNavWindow() {
    val navController = rememberNavController()

    AppNavHost(
        navController = navController,
        startDestination = WindowNavRoute.Home.route,
    ) {
        composable(WindowNavRoute.Home.route) {
            HomeScreen(navigateToSettings = { navController.navigate(WindowNavRoute.Settings.route) })
        }
        composable(WindowNavRoute.Settings.route) {
            SettingsScreen()
        }
    }
}

