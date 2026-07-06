package vision.combat.c4.ds.sample.bookmarks.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import vision.combat.c4.ds.sample.bookmarks.R
import vision.combat.c4.ds.sample.bookmarks.domain.model.Bookmark
import vision.combat.c4.ds.sample.bookmarks.ui.BookmarksViewModel.Action
import vision.combat.c4.ds.sample.bookmarks.ui.BookmarksViewModel.Event
import vision.combat.c4.ds.sample.bookmarks.ui.BookmarksViewModel.UiState
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.button.Button
import vision.combat.c4.ds.sdk.ui.component.button.OutlinedButton
import vision.combat.c4.ds.sdk.ui.component.list.ListItem
import vision.combat.c4.ds.sdk.ui.component.text.AppTextFieldDefaults
import vision.combat.c4.ds.sdk.ui.util.showToast
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel

/**
 * Single-screen entry point for the bookmarks sample. Resolves [BookmarksViewModel] via
 * [diViewModel], renders [UiState], and forwards [Action]s.
 */
@Composable
internal fun BookmarksWindow(viewModel: BookmarksViewModel = diViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WindowContent(uiState = uiState, onAction = viewModel::handleAction)
    EventHandler(eventFlow = viewModel.event)
}

@Composable
private fun WindowContent(uiState: UiState, onAction: (Action) -> Unit) {
    WindowScaffold(
        // The body is a LazyColumn, which must own its own scrolling — disable the scaffold's
        // default verticalScroll wrapper to avoid nesting a lazy list inside a scrollable parent.
        scrollable = false,
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.bookmarks_tool_name)) },
        content = { Content(uiState, onAction) },
    )
}

@Composable
private fun Content(uiState: UiState, onAction: (Action) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.bookmarks_explainer),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        Text(
            text = stringResource(R.string.bookmarks_section_add),
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        var label by rememberSaveable { mutableStateOf("") }
        var target by rememberSaveable { mutableStateOf("") }

        OutlinedTextField(
            value = label,
            onValueChange = { label = it },
            label = { Text(stringResource(R.string.bookmarks_label_hint)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = AppTextFieldDefaults.outlinedTextFieldColors(),
        )
        OutlinedTextField(
            value = target,
            onValueChange = { target = it },
            label = { Text(stringResource(R.string.bookmarks_target_hint)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = AppTextFieldDefaults.outlinedTextFieldColors(),
        )
        Button(
            label = stringResource(R.string.bookmarks_add),
            onClick = {
                onAction(Action.AddBookmark(label = label, target = target))
                label = ""
                target = ""
            },
        )

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        Text(
            text = stringResource(R.string.bookmarks_section_list),
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        if (uiState.bookmarks.isEmpty()) {
            Text(
                text = stringResource(R.string.bookmarks_empty),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(bottom = 16.dp),
            )
        } else {
            LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {
                items(uiState.bookmarks, key = { it.id }) { bookmark ->
                    BookmarkListItem(bookmark)
                }
            }
        }

        OutlinedButton(
            label = stringResource(R.string.bookmarks_clear),
            onClick = { onAction(Action.ClearBookmarks) },
        )
    }
}

@Composable
private fun BookmarkListItem(bookmark: Bookmark) {
    ListItem(
        headline = {
            Text(
                text = bookmark.label,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
            )
        },
        supportingText = {
            Text(
                text = bookmark.target,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface,
            )
        },
    )
}

@Composable
private fun EventHandler(eventFlow: Flow<Event>) {
    val context = LocalContext.current
    val addedMessage = stringResource(R.string.bookmarks_added_toast)
    LaunchedEffect(eventFlow) {
        eventFlow.collect { event ->
            when (event) {
                is Event.BookmarkAdded -> context.showToast(addedMessage)
            }
        }
    }
}
