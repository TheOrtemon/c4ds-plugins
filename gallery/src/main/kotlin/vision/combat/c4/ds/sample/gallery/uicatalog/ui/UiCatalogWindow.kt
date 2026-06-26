package vision.combat.c4.ds.sample.gallery.uicatalog.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import vision.combat.c4.ds.sample.gallery.uicatalog.UiCatalogEntry
import vision.combat.c4.ds.sdk.ui.navigation.AppNavHost

@Composable
internal fun UiCatalogWindow() {
    val navController = rememberNavController()

    AppNavHost(
        navController = navController,
        startDestination = UiCatalogRoute.List,
    ) {
        composable<UiCatalogRoute.List> {
            UiCatalogListScreen(
                onNavigateToDetail = { entry ->
                    navController.navigate(UiCatalogRoute.Detail(entry.name))
                },
            )
        }
        composable<UiCatalogRoute.Detail> { backStackEntry ->
            val route: UiCatalogRoute.Detail = backStackEntry.toRoute()
            val entry = UiCatalogEntry.valueOf(route.entryName)
            UiCatalogDetailScreen(
                entry = entry,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
