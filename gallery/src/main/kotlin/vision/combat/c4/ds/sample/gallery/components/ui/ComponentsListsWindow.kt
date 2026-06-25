package vision.combat.c4.ds.sample.gallery.components.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.reveal.RevealMenuButton
import vision.combat.c4.ds.sdk.ui.component.reveal.RevealableLazyColumn

private data class ListItem(val id: Int, val title: String)

@Composable
internal fun ComponentsListsWindow() {
    val items = remember {
        List(20) { i -> ListItem(id = i, title = "Item ${i + 1}") }
    }
    var refreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    WindowScaffold(
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.components_lists_tool_name)) },
        scrollable = false,
        contentPaddingValues = PaddingValues(0.dp),
    ) {
        RevealableLazyColumn(
            items = items,
            refreshing = refreshing,
            onRefresh = {
                refreshing = true
                scope.launch {
                    delay(1_500)
                    refreshing = false
                }
            },
            itemKey = { _, item -> item.id },
            startMenuItems = { _, _ ->
                RevealMenuButton(
                    painter = rememberVectorPainter(Icons.Default.Edit),
                    caption = stringResource(R.string.components_lists_action_edit),
                    onClick = {},
                )
            },
            endMenuItems = { _, _ ->
                RevealMenuButton(
                    painter = rememberVectorPainter(Icons.Default.Delete),
                    caption = stringResource(R.string.components_lists_action_delete),
                    onClick = {},
                )
            },
        ) { _, item ->
            ListRow(item)
        }
    }
}

@Composable
private fun ListRow(item: ListItem) {
    Text(
        text = item.title,
        style = MaterialTheme.typography.body1,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    )
    Divider()
}
