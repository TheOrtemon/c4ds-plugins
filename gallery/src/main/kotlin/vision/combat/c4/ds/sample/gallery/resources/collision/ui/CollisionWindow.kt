package vision.combat.c4.ds.sample.gallery.resources.collision.ui

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
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar

@Composable
internal fun CollisionWindow() {
    WindowScaffold(
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.collision_tool_name)) },
        content = { CollisionContent() },
    )
}

@Composable
private fun ColumnScope.CollisionContent() {
    Text(
        text = stringResource(R.string.collision_explainer),
        style = MaterialTheme.typography.body2,
        modifier = Modifier.padding(bottom = 16.dp),
    )

    Card(elevation = 4.dp, modifier = Modifier.padding(bottom = 12.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_collision),
                contentDescription = null,
                modifier = Modifier.size(32.dp).padding(end = 8.dp),
            )
            Text(
                text = "R.string.settings = \"${stringResource(R.string.settings)}\"",
                style = MaterialTheme.typography.body1,
            )
        }
    }

    Text(
        text = "Plugin-first resolution: the value shown above comes from the plugin APK, not the host.",
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.primary,
    )
}

