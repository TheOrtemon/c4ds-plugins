package vision.combat.c4.ds.sample.gallery.uicatalog.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import vision.combat.c4.ds.sdk.ui.navigation.AppNavHost

private const val ROUTE_LIST = "list"
private const val ROUTE_DETAIL = "detail/{componentId}"
private const val ARG_COMPONENT_ID = "componentId"

@Composable
internal fun UiCatalogWindow() {
    val navController = rememberNavController()

    AppNavHost(
        navController = navController,
        startDestination = ROUTE_LIST,
    ) {
        composable(ROUTE_LIST) {
            UiCatalogListScreen(
                onNavigateToDetail = { componentId ->
                    navController.navigate("detail/$componentId")
                },
            )
        }
        composable(
            route = ROUTE_DETAIL,
            arguments = listOf(navArgument(ARG_COMPONENT_ID) { type = NavType.StringType }),
        ) { backStackEntry ->
            val componentId = backStackEntry.arguments?.getString(ARG_COMPONENT_ID) ?: return@composable
            UiCatalogDetailScreen(
                componentId = componentId,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
