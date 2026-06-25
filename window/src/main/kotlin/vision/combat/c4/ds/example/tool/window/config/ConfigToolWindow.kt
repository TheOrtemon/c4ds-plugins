package vision.combat.c4.ds.example.tool.window.config

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.tool.sample.window.R

/**
 * Case (c): Plugin resources react to locale / night-mode / rotation changes.
 * CompositionFallbackContext is rebuilt on config change, so:
 *  - stringResource(R.string.config_mode) picks up values-night/ or values-uk/ variant.
 *  - painterResource(R.drawable.ic_daynight) picks up drawable-night/ variant.
 * Manual check: toggle dark mode or switch locale while this window is open.
 */
@Composable
internal fun ConfigToolWindow() {
    WindowScaffold(
        topAppBar = {
            BackNavTopAppBar(title = stringResource(R.string.config_tool_name))
        },
        content = { ConfigContent() },
    )
}

@Composable
private fun ColumnScope.ConfigContent() {
    Text(
        text = stringResource(R.string.config_explainer),
        style = MaterialTheme.typography.body2,
        modifier = Modifier.padding(bottom = 16.dp),
    )

    Card(
        elevation = 4.dp,
        modifier = Modifier.padding(bottom = 12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_daynight),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 12.dp),
            )
            Text(
                text = stringResource(R.string.config_mode),
                style = MaterialTheme.typography.h6,
            )
        }
    }

    Text(
        text = "Toggle system dark mode or switch locale — the string and icon above should update live.",
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.secondary,
    )
}
