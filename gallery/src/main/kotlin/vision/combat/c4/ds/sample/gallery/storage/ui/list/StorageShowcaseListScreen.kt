package vision.combat.c4.ds.sample.gallery.storage.ui.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
 * Showcase index for the Storage hub. Tapping a row opens its detail screen.
 */
@Composable
internal fun StorageShowcaseListScreen(
    onNavigateToDetail: (StorageShowcase) -> Unit,
) {
    WindowScaffold(
        scrollable = false,
        contentPaddingValues = PaddingValues(0.dp),
        topAppBar = {
            BackNavTopAppBar(title = stringResource(R.string.storage_tool_name))
        },
        content = {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(StorageShowcase.entries, key = { _, showcase -> showcase.name }) { index, showcase ->
                    ShowcaseListItem(
                        showcase = showcase,
                        onClick = { onNavigateToDetail(showcase) },
                        showDivider = index != StorageShowcase.entries.lastIndex,
                    )
                }
            }
        },
    )
}

@Composable
private fun ShowcaseListItem(
    showcase: StorageShowcase,
    onClick: () -> Unit,
    showDivider: Boolean = true,
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
        showDivider = showDivider,
    )
}
