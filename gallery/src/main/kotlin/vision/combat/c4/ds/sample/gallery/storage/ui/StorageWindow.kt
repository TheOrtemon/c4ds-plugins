package vision.combat.c4.ds.sample.gallery.storage.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import vision.combat.c4.ds.sample.gallery.storage.ui.detail.StorageShowcaseDetailScreen
import vision.combat.c4.ds.sample.gallery.storage.ui.list.StorageShowcase
import vision.combat.c4.ds.sample.gallery.storage.ui.list.StorageShowcaseListScreen
import vision.combat.c4.ds.sdk.ui.navigation.AppNavHost

/**
 * Root window of the Storage showcase hub: a list of three showcases (File, Preferences, Room),
 * each navigating to a dedicated detail screen. Uses type-safe routes + [AppNavHost].
 */
@Composable
internal fun StorageWindow() {
    val navController = rememberNavController()

    AppNavHost(
        navController = navController,
        startDestination = StorageRoute.List,
    ) {
        composable<StorageRoute.List> {
            StorageShowcaseListScreen(
                onNavigateToDetail = { showcase ->
                    navController.navigate(StorageRoute.Detail(showcase.name))
                },
            )
        }
        composable<StorageRoute.Detail> { backStackEntry ->
            val route: StorageRoute.Detail = backStackEntry.toRoute()
            StorageShowcaseDetailScreen(showcase = StorageShowcase.valueOf(route.showcaseName))
        }
    }
}
