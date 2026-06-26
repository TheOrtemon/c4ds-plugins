package vision.combat.c4.ds.sample.gallery.window.multiscreen.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.window.multiscreen.ui.HomeViewModel.Action
import vision.combat.c4.ds.sample.gallery.window.multiscreen.ui.HomeViewModel.Event
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.button.TextButton
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel

@Composable
internal fun HomeScreen(navigateToSettings: () -> Unit) {
    val viewModel = diViewModel<HomeViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Content(
        uiState = uiState,
        onAction = viewModel::handleAction,
    )

    EventHandler(
        events = viewModel.events,
        navigateToSettings = navigateToSettings,
    )
}

@Composable
private fun Content(
    uiState: HomeViewModel.UiState,
    onAction: (Action) -> Unit,
) {
    WindowScaffold(
        topAppBar = {
            BackNavTopAppBar(
                title = stringResource(R.string.window_multi_screen_home_title),
                actions = {
                    TextButton(
                        label = stringResource(R.string.window_multi_screen_go_settings),
                        onClick = { onAction(Action.OpenSettings) },
                    )
                },
            )
        },
        content = { HomeContent(uiState) },
    )
}

@Composable
private fun ColumnScope.HomeContent(uiState: HomeViewModel.UiState) {
    if (uiState.showDescription) {
        Text(
            text = stringResource(R.string.window_multi_screen_home_desc),
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(bottom = 16.dp),
        )
    }
    Text(
        text = stringResource(R.string.window_multi_screen_home_hint),
        style = MaterialTheme.typography.caption,
        modifier = Modifier.padding(bottom = 8.dp),
    )
}

@Composable
private fun EventHandler(
    events: Flow<Event>,
    navigateToSettings: () -> Unit,
) {
    LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                Event.NavigateToSettings -> navigateToSettings()
            }
        }
    }
}
