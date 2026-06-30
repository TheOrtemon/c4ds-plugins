package vision.combat.c4.ds.sample.gallery.mapinteractor.ui.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.list.ListItem

/**
 * Showcase index for the Map interactor sample. Tapping a row opens its detail screen.
 */
@Composable
internal fun MapInteractorShowcaseListScreen(
    onNavigateToDetail: (MapInteractorShowcase) -> Unit,
) {
    WindowScaffold(
        // LazyColumn owns its own scrolling — disable the scaffold's verticalScroll wrapper.
        scrollable = false,
        contentPaddingValues = PaddingValues(0.dp),
        topAppBar = {
            BackNavTopAppBar(title = stringResource(R.string.map_interactor_tool_name))
        },
        content = {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(MapInteractorShowcase.entries, key = { it.name }) { showcase ->
                    ShowcaseListItem(
                        showcase = showcase,
                        onClick = { onNavigateToDetail(showcase) },
                    )
                }
            }
        },
    )
}

@Composable
private fun ShowcaseListItem(
    showcase: MapInteractorShowcase,
    onClick: () -> Unit,
) {
    ListItem(
        headline = {
            Text(
                text = stringResource(showcase.nameResId),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onSurface,
            )
        },
        supportingText = {
            Text(
                text = stringResource(showcase.descResId),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface,
            )
        },
        onItemClick = onClick,
    )
}
