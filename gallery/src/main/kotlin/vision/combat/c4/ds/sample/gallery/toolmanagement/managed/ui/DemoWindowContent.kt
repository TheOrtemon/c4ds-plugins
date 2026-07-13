package vision.combat.c4.ds.sample.gallery.toolmanagement.managed.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar

/**
 * Content of [vision.combat.c4.ds.sample.gallery.toolmanagement.managed.DemoWindowTool]'s window.
 *
 * Shows which activation flag opened it, and — when it was opened with
 * `FLAG_NONE` — a warning that Back navigation will land on the root Tools list with the Tool
 * Management sample deactivated, because [FLAG_NONE][vision.combat.c4.ds.sdk.tool.ToolManager.Companion.FLAG_NONE]
 * clears the window stack and Tool Management's window is a required component.
 */
@Composable
internal fun DemoWindowContent(openedWithReplaceFlag: Boolean) {
    WindowScaffold(
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.tool_management_demo_window_title)) },
        content = { Content(openedWithReplaceFlag) },
    )
}

@Composable
private fun ColumnScope.Content(openedWithReplaceFlag: Boolean) {
    Text(
        text = if (openedWithReplaceFlag) {
            stringResource(R.string.tool_management_demo_window_mode_replace)
        } else {
            stringResource(R.string.tool_management_demo_window_mode_on_top)
        },
        style = MaterialTheme.typography.body1,
        modifier = Modifier.padding(bottom = 12.dp),
    )

    if (openedWithReplaceFlag) {
        Spacer(modifier = Modifier.height(4.dp))
        Card(elevation = 1.dp) {
            Text(
                text = stringResource(R.string.tool_management_demo_window_replace_warning),
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(12.dp),
            )
        }
    }
}
