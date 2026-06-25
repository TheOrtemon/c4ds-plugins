package vision.combat.c4.ds.example.tool.window.fontraw

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.tool.sample.window.R

/**
 * Case (e): Plugin font (R.font.sample_font — Roboto Regular, OFL) and raw resource (R.raw.sample_note)
 * both resolve via FallbackResources(plugin, host) at composition time.
 * Font(R.font.sample_font) resolves through the plugin's ResourcesCompat context.
 * resources.openRawResource(R.raw.sample_note) reads from the plugin APK.
 */
@Composable
internal fun FontRawToolWindow() {
    val context = LocalContext.current

    val rawNote = remember(context) {
        runCatching {
            context.resources.openRawResource(R.raw.sample_note).bufferedReader().use { it.readText() }
        }.getOrElse { e -> "Error reading raw resource: ${e.message}" }
    }

    val pluginFont = remember { FontFamily(Font(R.font.sample_font)) }

    WindowScaffold(
        topAppBar = {
            BackNavTopAppBar(title = stringResource(R.string.fontraw_tool_name))
        },
        content = { FontRawContent(rawNote = rawNote, pluginFont = pluginFont) },
    )
}

@Composable
private fun ColumnScope.FontRawContent(rawNote: String, pluginFont: FontFamily) {
    Text(
        text = stringResource(R.string.fontraw_explainer),
        style = MaterialTheme.typography.body2,
        modifier = Modifier.padding(bottom = 12.dp),
    )

    Text(
        text = stringResource(R.string.fontraw_font_label),
        style = MaterialTheme.typography.caption,
        modifier = Modifier.padding(bottom = 4.dp),
    )
    Text(
        text = "The quick brown fox jumps over the lazy dog — plugin font (Roboto Regular, OFL)",
        fontFamily = pluginFont,
        style = MaterialTheme.typography.body1,
        modifier = Modifier.padding(bottom = 16.dp),
    )

    Divider(modifier = Modifier.padding(bottom = 12.dp))

    Text(
        text = stringResource(R.string.fontraw_raw_label),
        style = MaterialTheme.typography.caption,
        modifier = Modifier.padding(bottom = 4.dp),
    )
    Card(elevation = 2.dp) {
        Text(
            text = rawNote,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(12.dp),
        )
    }
}
