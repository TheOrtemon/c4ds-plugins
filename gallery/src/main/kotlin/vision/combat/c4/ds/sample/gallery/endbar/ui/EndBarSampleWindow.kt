package vision.combat.c4.ds.sample.gallery.endbar.ui

import androidx.compose.foundation.layout.ColumnScope
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

@Composable
internal fun EndBarSampleWindow(
    toggleState: () -> Boolean,
    sliderValue: () -> Float,
) {
    WindowScaffold(
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.endbar_tool_name)) },
        content = { EndBarContent(toggleState, sliderValue) },
    )
}

@Composable
private fun ColumnScope.EndBarContent(
    toggleState: () -> Boolean,
    sliderValue: () -> Float,
) {
    Text(
        text = stringResource(R.string.endbar_explainer),
        style = MaterialTheme.typography.body2,
        modifier = Modifier.padding(bottom = 16.dp),
    )
    Card(elevation = 2.dp, modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            text = "${stringResource(R.string.endbar_current_toggle)} ${if (toggleState()) "ON" else "OFF"}",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(16.dp),
        )
    }
    Card(elevation = 2.dp) {
        Text(
            text = "${stringResource(R.string.endbar_current_slider)} ${"%.2f".format(sliderValue())}",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(16.dp),
        )
    }
}

