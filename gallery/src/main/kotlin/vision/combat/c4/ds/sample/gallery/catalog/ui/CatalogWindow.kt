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
            CatalogCategoryListScreen(
                onNavigateToCategory = { section ->
                    navController.navigate(CatalogRoute.CategoryDetail(section.name))
                },
                onNavigateToDetail = { entry ->
                    navController.navigate(CatalogRoute.Detail(entry.name))
                },
            )
        }
        composable<CatalogRoute.CategoryDetail> { backStackEntry ->
            val route: CatalogRoute.CategoryDetail = backStackEntry.toRoute()
            val section = CatalogSection.valueOf(route.sectionName)

            CatalogCategoryDetailScreen(
                section = section,
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
