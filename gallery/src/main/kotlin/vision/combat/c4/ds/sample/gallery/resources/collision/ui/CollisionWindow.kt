package vision.combat.c4.ds.sample.gallery.resources.collision.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
    // Live, plugin-first resolution: inside a plugin tool tree LocalContext.current.resources is the
    // composition context backed by FallbackResources, which resolves the plugin's R.string.settings
    // id against the plugin table first — so this is the plugin's value, not the host's.
    val resolved = stringResource(R.string.settings)

    Text(
        text = stringResource(R.string.collision_explainer),
        style = MaterialTheme.typography.body2,
        modifier = Modifier.padding(bottom = 16.dp),
    )

    Card(elevation = 4.dp, modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.ic_collision),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp).padding(end = 8.dp),
                )
                Text(
                    text = stringResource(R.string.collision_key_header),
                    style = MaterialTheme.typography.subtitle1,
                    fontFamily = FontFamily.Monospace,
                )
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            // Host row: shown for reference only — the plugin cannot read the host value at runtime
            // (per-APK isolation), so it is a documented constant that mirrors c4ds-app's R.string.settings.
            DeclarationRow(
                label = stringResource(R.string.collision_host_label),
                value = stringResource(R.string.collision_host_value),
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Plugin row: the live resolved value — what R.string.settings actually returns.
            DeclarationRow(
                label = stringResource(R.string.collision_plugin_label),
                value = resolved,
            )

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.collision_resolved_label),
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = resolved,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "✅ ${stringResource(R.string.collision_resolved_badge)}",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface,
                )
            }
        }
    }

    Text(
        text = stringResource(R.string.collision_plugin_first_caption),
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.onSurface,
    )
}

@Composable
private fun DeclarationRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f),
        )
    }
}
