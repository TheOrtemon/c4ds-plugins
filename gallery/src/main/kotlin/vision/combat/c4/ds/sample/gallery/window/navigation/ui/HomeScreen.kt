package vision.combat.c4.ds.sample.gallery.window.navigation.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import org.kodein.di.compose.rememberInstance
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.window.navigation.ui.HomeViewModel.Action
import vision.combat.c4.ds.sample.gallery.window.navigation.ui.HomeViewModel.Event
import vision.combat.c4.ds.sample.gallery.window.simple.WindowSimpleToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolId
import vision.combat.c4.ds.sdk.tool.ToolManager
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.button.Button
import vision.combat.c4.ds.sdk.ui.component.button.TextButton
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel
import vision.combat.c4.ds.sample.gallery.BuildConfig

@Composable
internal fun HomeScreen(navigateToSettings: () -> Unit) {
    val viewModel = diViewModel<HomeViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    ScreenContent(
        uiState = uiState,
        navigateToSettings = navigateToSettings,
        onAction = viewModel::handleAction,
    )
    EventHandler(eventFlow = viewModel.event)
}

@Composable
private fun ScreenContent(
    uiState: HomeViewModel.UiState,
    navigateToSettings: () -> Unit,
    onAction: (Action) -> Unit,
) {
    WindowScaffold(
        topAppBar = {
            BackNavTopAppBar(
                title = stringResource(R.string.window_nav_home_title),
                actions = {
                    TextButton(
                        label = stringResource(R.string.window_nav_go_settings),
                        onClick = navigateToSettings,
                    )
                },
            )
        },
        content = { HomeContent(uiState, onAction) },
    )
}

@Composable
private fun ColumnScope.HomeContent(uiState: HomeViewModel.UiState, onAction: (Action) -> Unit) {
    Text(
        text = stringResource(R.string.window_nav_home_desc),
        style = MaterialTheme.typography.body2,
        modifier = Modifier.padding(bottom = 16.dp),
    )
    Button(
        label = stringResource(R.string.window_nav_launch_simple),
        onClick = {
            val simpleToolId = ToolId<WindowSimpleToolDescriptor>(BuildConfig.APPLICATION_ID)
            onAction(Action.ActivateTool(simpleToolId, uiState.openOnTop))
        },
    )
}

@Composable
private fun EventHandler(eventFlow: Flow<Event>) {
    val toolManager by rememberInstance<ToolManager>()
    LaunchedEffect(eventFlow) {
        eventFlow.collect { event ->
            when (event) {
                is Event.ToolActivationRequested -> {
                    val flags = if (event.onTop) ToolManager.FLAG_COMPONENT_ON_TOP else ToolManager.FLAG_NONE
                    toolManager.activate(event.toolId, flags)
                }
            }
        }
    }
}

