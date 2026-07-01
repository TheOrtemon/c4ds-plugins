package vision.combat.c4.ds.sample.gallery.window.singlescreen.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.window.singlescreen.ui.WindowSingleScreenViewModel.Action
import vision.combat.c4.ds.sample.gallery.window.singlescreen.ui.WindowSingleScreenViewModel.Event
import vision.combat.c4.ds.sample.gallery.window.singlescreen.ui.WindowSingleScreenViewModel.UiState
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.button.Button
import vision.combat.c4.ds.sdk.ui.component.button.TextButton
import vision.combat.c4.ds.sdk.ui.util.showToast
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel

@Composable
internal fun WindowSingleScreenWindow(viewModel: WindowSingleScreenViewModel = diViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WindowContent(uiState = uiState, onAction = viewModel::handleAction)
    EventHandler(eventFlow = viewModel.event)
}

@Composable
private fun WindowContent(uiState: UiState, onAction: (Action) -> Unit) {
    WindowScaffold(
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.window_single_screen_tool_name)) },
        content = { Content(uiState, onAction) },
    )
}

@Composable
private fun ColumnScope.Content(uiState: UiState, onAction: (Action) -> Unit) {
    Text(
        text = stringResource(R.string.window_single_screen_desc_short),
        style = MaterialTheme.typography.body2,
        modifier = Modifier.padding(bottom = 16.dp),
    )
    Text(
        text = stringResource(R.string.window_single_screen_counter_label, uiState.count),
        style = MaterialTheme.typography.h5,
        modifier = Modifier.padding(bottom = 8.dp),
    )
    Spacer(modifier = Modifier.height(8.dp))
    Button(
        label = stringResource(R.string.window_single_screen_increment),
        onClick = { onAction(Action.Increment) },
    )
    TextButton(
        label = stringResource(R.string.window_single_screen_reset),
        onClick = { onAction(Action.Reset) },
    )
}

@Composable
private fun EventHandler(eventFlow: Flow<Event>) {
    val context = LocalContext.current
    val resetMessage = stringResource(R.string.window_single_screen_reset_toast)
    LaunchedEffect(eventFlow) {
        eventFlow.collect { event ->
            when (event) {
                is Event.CounterReset -> context.showToast(resetMessage)
            }
        }
    }
}
