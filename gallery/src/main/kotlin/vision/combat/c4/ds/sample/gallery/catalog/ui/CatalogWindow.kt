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
                onNavigateToDetail = { sampleId ->
                    navController.navigate(CatalogRoute.Detail(sampleId))
                },
            )
        }
        composable<CatalogRoute.Detail> { backStackEntry ->
            val route: CatalogRoute.Detail = backStackEntry.toRoute()
            CatalogDetailScreen(
                sampleId = route.sampleId,
            )
        }
    }
}
