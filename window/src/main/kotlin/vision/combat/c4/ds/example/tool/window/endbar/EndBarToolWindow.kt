package vision.combat.c4.ds.example.tool.window.endbar

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.tool.sample.window.R

@Composable
internal fun EndBarToolWindow(isToggled: Boolean) {
    WindowScaffold(
        topAppBar = {
            BackNavTopAppBar(title = stringResource(R.string.endbar_tool_name))
        },
        content = { EndBarContent(isToggled) },
    )
}

@Composable
private fun ColumnScope.EndBarContent(isToggled: Boolean) {
    Text(
        text = stringResource(R.string.endbar_explainer),
        style = MaterialTheme.typography.body2,
        modifier = Modifier.padding(bottom = 16.dp),
    )

    Card(elevation = 2.dp) {
        Text(
            text = "Toggle button state: ${if (isToggled) "ON" else "OFF"}",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(16.dp),
        )
    }

    Text(
        text = "Look for the EndBar on the side of the host UI. Tap the action button (no-op) and toggle button to see state reflected above.",
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.secondary,
        modifier = Modifier.padding(top = 12.dp),
    )
}
