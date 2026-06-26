package vision.combat.c4.ds.sample.gallery.catalog.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import vision.combat.c4.ds.sdk.ui.navigation.AppNavHost

private const val ROUTE_LIST = "list"
private const val ROUTE_DETAIL = "detail/{sampleId}"
private const val ARG_SAMPLE_ID = "sampleId"

@Composable
internal fun CatalogWindow() {
    val navController = rememberNavController()
    var crossApkRefreshKey by remember { mutableIntStateOf(0) }

    AppNavHost(
        navController = navController,
        startDestination = ROUTE_LIST,
    ) {
        composable(ROUTE_LIST) {
            CatalogListScreen(
                crossApkRefreshKey = crossApkRefreshKey,
                onNavigateToDetail = { sampleId ->
                    navController.navigate("detail/$sampleId")
                },
            )
        }
        composable(
            route = ROUTE_DETAIL,
            arguments = listOf(navArgument(ARG_SAMPLE_ID) { type = NavType.StringType }),
        ) { backStackEntry ->
            val sampleId = backStackEntry.arguments?.getString(ARG_SAMPLE_ID) ?: return@composable
            CatalogDetailScreen(
                sampleId = sampleId,
                onBack = {
                    crossApkRefreshKey++
                    navController.popBackStack()
                },
            )
        }
    }
}

