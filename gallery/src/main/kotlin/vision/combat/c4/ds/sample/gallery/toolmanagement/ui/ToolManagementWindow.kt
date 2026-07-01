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
        text = stringResource(R.string.tool_management_map_tool_section),
        style = MaterialTheme.typography.subtitle1,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 8.dp),
    )

    Card(elevation = 2.dp, modifier = Modifier.padding(bottom = 12.dp)) {
        Text(
            text = "${stringResource(R.string.tool_management_map_active)} ${if (uiState.isMapToolActive) stringResource(R.string.tool_management_active_yes) else stringResource(R.string.tool_management_active_no)}",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(16.dp),
        )
    }

    Button(
        label = stringResource(R.string.tool_management_activate_map),
        onClick = viewModel::activateMapTool,
        enabled = !uiState.isMapToolActive,
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedButton(
        label = stringResource(R.string.tool_management_deactivate_map),
        onClick = viewModel::deactivateMapTool,
        enabled = uiState.isMapToolActive,
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedButton(
        label = stringResource(R.string.tool_management_show_map_window),
        onClick = viewModel::showMapWindow,
        enabled = uiState.isMapToolActive,
    )

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
