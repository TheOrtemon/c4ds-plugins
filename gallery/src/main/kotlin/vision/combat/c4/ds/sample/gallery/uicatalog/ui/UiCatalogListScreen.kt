package vision.combat.c4.ds.sample.gallery.uicatalog.ui

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
import vision.combat.c4.ds.sample.gallery.uicatalog.UiCatalogEntry
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.list.ListItem

@Composable
internal fun UiCatalogListScreen(
    onNavigateToDetail: (UiCatalogEntry) -> Unit,
) {
    WindowScaffold(
        // The body is a LazyColumn, which must own its own scrolling — disable the scaffold's
        // default verticalScroll wrapper to avoid nesting a lazy list inside a scrollable parent.
        scrollable = false,
        contentPaddingValues = PaddingValues(0.dp),
        topAppBar = {
            BackNavTopAppBar(title = stringResource(R.string.ui_catalog_tool_name))
        },
        content = {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 4.dp),
            ) {
                items(UiCatalogEntry.entries, key = { it.name }) { entry ->
                    ComponentListItem(
                        entry = entry,
                        onClick = { onNavigateToDetail(entry) },
                    )
                }
            }
        },
    )
}

@Composable
private fun ComponentListItem(
    entry: UiCatalogEntry,
    onClick: () -> Unit,
) {
    ListItem(
        headline = {
            Text(
                text = stringResource(entry.nameResId),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onSurface,
            )
        },
        supportingText = {
            Text(
                text = stringResource(entry.descResId),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface,
            )
        },
        onItemClick = onClick,
    )
}
