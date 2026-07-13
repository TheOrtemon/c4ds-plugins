package vision.combat.c4.ds.sample.gallery.mapview.mapinteractor.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import vision.combat.c4.ds.sample.gallery.mapview.mapinteractor.ui.detail.MapInteractorShowcaseDetailScreen
import vision.combat.c4.ds.sample.gallery.mapview.mapinteractor.ui.list.MapInteractorShowcase
import vision.combat.c4.ds.sample.gallery.mapview.mapinteractor.ui.list.MapInteractorShowcaseListScreen
import vision.combat.c4.ds.sdk.ui.navigation.AppNavHost

/**
 * Root window of the Map interactor sample: a list of showcases, each navigating to a dedicated
 * detail screen. Mirrors the UI Catalog navigation pattern (type-safe routes + [AppNavHost]).
 */
@Composable
internal fun MapInteractorWindow() {
    val navController = rememberNavController()

    AppNavHost(
        navController = navController,
        startDestination = MapInteractorRoute.List,
    ) {
        composable<MapInteractorRoute.List> {
            MapInteractorShowcaseListScreen(
                onNavigateToDetail = { showcase ->
                    navController.navigate(MapInteractorRoute.Detail(showcase.name))
                },
            )
        }
        composable<MapInteractorRoute.Detail> { backStackEntry ->
            val route: MapInteractorRoute.Detail = backStackEntry.toRoute()
            MapInteractorShowcaseDetailScreen(showcase = MapInteractorShowcase.valueOf(route.showcaseName))
        }
    }
}
