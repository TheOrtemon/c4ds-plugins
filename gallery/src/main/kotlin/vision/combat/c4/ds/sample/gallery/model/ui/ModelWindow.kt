package vision.combat.c4.ds.sample.gallery.model.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import vision.combat.c4.ds.sample.gallery.model.ui.detail.ModelShowcaseDetailScreen
import vision.combat.c4.ds.sample.gallery.model.ui.list.ModelShowcase
import vision.combat.c4.ds.sample.gallery.model.ui.list.ModelShowcaseListScreen
import vision.combat.c4.ds.sdk.ui.navigation.AppNavHost

/**
 * Root window of the Model interactor sample: a list of showcases, each navigating to a dedicated
 * detail screen. Mirrors the UI Catalog navigation pattern (type-safe routes + [AppNavHost]).
 */
@Composable
internal fun ModelWindow() {
    val navController = rememberNavController()

    AppNavHost(
        navController = navController,
        startDestination = ModelRoute.List,
    ) {
        composable<ModelRoute.List> {
            ModelShowcaseListScreen(
                onNavigateToDetail = { showcase ->
                    navController.navigate(ModelRoute.Detail(showcase.name))
                },
            )
        }
        composable<ModelRoute.Detail> { backStackEntry ->
            val route: ModelRoute.Detail = backStackEntry.toRoute()
            ModelShowcaseDetailScreen(showcase = ModelShowcase.valueOf(route.showcaseName))
        }
    }
}
