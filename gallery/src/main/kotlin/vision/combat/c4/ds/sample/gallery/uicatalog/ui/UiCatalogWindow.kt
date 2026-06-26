package vision.combat.c4.ds.sample.gallery.uicatalog.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
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
                onNavigateToDetail = { componentId ->
                    navController.navigate(UiCatalogRoute.Detail(componentId))
                },
            )
        }
        composable<UiCatalogRoute.Detail> { backStackEntry ->
            val route: UiCatalogRoute.Detail = backStackEntry.toRoute()
            UiCatalogDetailScreen(
                componentId = route.componentId,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
