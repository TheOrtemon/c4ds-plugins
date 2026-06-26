package vision.combat.c4.ds.sample.gallery.catalog.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import vision.combat.c4.ds.sdk.ui.navigation.AppNavHost

@Composable
internal fun CatalogWindow() {
    val navController = rememberNavController()

    AppNavHost(
        navController = navController,
        startDestination = CatalogRoute.List,
    ) {
        composable<CatalogRoute.List> {
            CatalogListScreen(
                onNavigateToDetail = { entry ->
                    navController.navigate(CatalogRoute.Detail(entry.name))
                },
            )
        }
        composable<CatalogRoute.Detail> { backStackEntry ->
            val route: CatalogRoute.Detail = backStackEntry.toRoute()
            val entry = CatalogEntry.valueOf(route.entryName)

            CatalogDetailScreen(entry = entry)
        }
    }
}
