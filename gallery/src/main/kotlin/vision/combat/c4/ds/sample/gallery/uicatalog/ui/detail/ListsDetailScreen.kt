package vision.combat.c4.ds.sample.gallery.uicatalog.ui.detail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.uicatalog.ui.list.UiCatalogEntry
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavigationButton
import vision.combat.c4.ds.sdk.ui.component.bar.TopAppBar
import vision.combat.c4.ds.sdk.ui.component.list.ListItem
import vision.combat.c4.ds.sdk.ui.component.reveal.DeleteMenuButton
import vision.combat.c4.ds.sdk.ui.component.reveal.EditMenuButton
import vision.combat.c4.ds.sdk.ui.component.reveal.RevealableLazyColumn
import kotlin.time.Duration.Companion.milliseconds

private data class RevealItem(val id: Int)

@Composable
internal fun ListsDetailScreen(entry: UiCatalogEntry, onBack: () -> Unit) {
    val items = remember { List(20) { i -> RevealItem(id = i) } }
    var refreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    WindowScaffold(
        scrollable = false,
        contentPaddingValues = PaddingValues(0.dp),
        topAppBar = {
            TopAppBar(
                title = stringResource(entry.nameResId),
                navigationIcon = { BackNavigationButton(onBack) },
            )
        },
    ) {
        RevealableLazyColumn(
            modifier = Modifier.fillMaxSize(),
            items = items,
            refreshing = refreshing,
            onRefresh = {
                refreshing = true
                scope.launch {
                    delay(1_500.milliseconds)
                    refreshing = false
                }
            },
            itemKey = { _, item -> item.id },
            startMenuItems = { _, _ ->
                EditMenuButton(onClick = {})
            },
            endMenuItems = { _, _ ->
                DeleteMenuButton(onClick = {})
            },
        ) { _, item ->
            ListItem(
                headline = {
                    Text(
                        text = stringResource(R.string.ui_catalog_list_item, item.id + 1),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface,
                    )
                },
                onItemClick = {},
            )
        }
    }
}
