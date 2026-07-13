package vision.combat.c4.ds.sample.gallery.toolmanagement.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.button.Button
import vision.combat.c4.ds.sdk.ui.component.button.OutlinedButton
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel

@Composable
internal fun ToolManagementWindow(viewModel: ToolManagementViewModel = diViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WindowScaffold(
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.tool_management_tool_name)) },
        content = { ToolManagementContent(uiState, viewModel) },
    )
}

@Composable
private fun ColumnScope.ToolManagementContent(
    uiState: ToolManagementViewModel.UiState,
    viewModel: ToolManagementViewModel,
) {
    Text(
        text = stringResource(R.string.tool_management_explainer),
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 16.dp),
    )

    Text(
        text = stringResource(R.string.tool_management_demo_tool_section),
        style = MaterialTheme.typography.subtitle1,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 8.dp),
    )

    Card(elevation = 2.dp, modifier = Modifier.padding(bottom = 12.dp)) {
        Text(
            text = "${stringResource(R.string.tool_management_demo_active)} ${if (uiState.isDemoToolActive) stringResource(R.string.tool_management_active_yes) else stringResource(R.string.tool_management_active_no)}",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(16.dp),
        )
    }

    Button(
        label = stringResource(R.string.tool_management_activate_demo),
        onClick = viewModel::activateDemoTool,
        enabled = !uiState.isDemoToolActive,
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedButton(
        label = stringResource(R.string.tool_management_deactivate_demo),
        onClick = viewModel::deactivateDemoTool,
        enabled = uiState.isDemoToolActive,
    )

    Divider(modifier = Modifier.padding(vertical = 16.dp))

    Text(
        text = stringResource(R.string.tool_management_demo_components_section),
        style = MaterialTheme.typography.subtitle1,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 8.dp),
    )

    Text(
        text = stringResource(R.string.tool_management_demo_components_explainer),
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 12.dp),
    )

    Button(
        label = stringResource(R.string.tool_management_show_demo_overlay),
        onClick = viewModel::showDemoOverlay,
        enabled = uiState.isDemoToolActive && !uiState.isDemoOverlayShown,
    )
    Spacer(modifier = Modifier.height(4.dp))
    OutlinedButton(
        label = stringResource(R.string.tool_management_hide_demo_overlay),
        onClick = viewModel::hideDemoOverlay,
        enabled = uiState.isDemoToolActive && uiState.isDemoOverlayShown,
    )

    Spacer(modifier = Modifier.height(12.dp))

    Button(
        label = stringResource(R.string.tool_management_show_demo_status),
        onClick = viewModel::showDemoStatus,
        enabled = uiState.isDemoToolActive && !uiState.isDemoStatusShown,
    )
    Spacer(modifier = Modifier.height(4.dp))
    OutlinedButton(
        label = stringResource(R.string.tool_management_hide_demo_status),
        onClick = viewModel::hideDemoStatus,
        enabled = uiState.isDemoToolActive && uiState.isDemoStatusShown,
    )

    Divider(modifier = Modifier.padding(vertical = 16.dp))

    Text(
        text = stringResource(R.string.tool_management_window_flags_section),
        style = MaterialTheme.typography.subtitle1,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 8.dp),
    )

    Text(
        text = stringResource(R.string.tool_management_window_flags_explainer),
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 12.dp),
    )

    Button(
        label = stringResource(R.string.tool_management_open_demo_on_top),
        onClick = viewModel::openDemoWindowOnTop,
        enabled = !uiState.isDemoWindowToolActive,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Button(
        label = stringResource(R.string.tool_management_open_demo_replace),
        onClick = viewModel::openDemoWindowReplace,
        enabled = !uiState.isDemoWindowToolActive,
    )

    Spacer(modifier = Modifier.height(12.dp))

    Card(elevation = 1.dp, modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            text = stringResource(R.string.tool_management_window_flags_warning),
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(12.dp),
        )
    }

    Divider(modifier = Modifier.padding(vertical = 16.dp))

    Text(
        text = stringResource(R.string.tool_management_active_tools_section),
        style = MaterialTheme.typography.subtitle1,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 8.dp),
    )

    if (uiState.activeToolClassNames.isEmpty()) {
        Text(
            text = stringResource(R.string.tool_management_no_active_tools),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
        )
    } else {
        uiState.activeToolClassNames.forEach { className ->
            val simpleClassName = className.substringAfterLast('.')
            Card(elevation = 1.dp, modifier = Modifier.padding(bottom = 4.dp)) {
                Text(
                    text = simpleClassName,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                )
            }
        }
    }
}
