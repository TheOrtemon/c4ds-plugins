package vision.combat.c4.ds.sample.gallery.resources.config.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar

@Composable
internal fun ConfigWindow() {
    WindowScaffold(
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.config_tool_name)) },
        content = { ConfigContent() },
    )
}

@Composable
private fun ColumnScope.ConfigContent() {
    val context = LocalContext.current

    val rawNote = remember(context) {
        runCatching {
            context.resources.openRawResource(R.raw.sample_note).bufferedReader().use { it.readText() }
        }.getOrElse { e -> context.getString(R.string.config_error_raw_resource, e.message ?: "") }
    }

    val pluginFont = remember { FontFamily(Font(R.font.sample_font)) }

    Text(
        text = stringResource(R.string.config_explainer),
        style = MaterialTheme.typography.body2,
        modifier = Modifier.padding(bottom = 12.dp),
    )

    Card(elevation = 4.dp, modifier = Modifier.padding(bottom = 12.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_resources),
                contentDescription = null,
                modifier = Modifier.size(48.dp).padding(end = 12.dp),
            )
            Text(
                text = stringResource(R.string.config_mode),
                style = MaterialTheme.typography.h6,
            )
        }
    }

    Divider(modifier = Modifier.padding(vertical = 8.dp))

    Text(stringResource(R.string.config_font_label), style = MaterialTheme.typography.caption)
    Text(
        text = stringResource(R.string.config_font_sample),
        fontFamily = pluginFont,
        style = MaterialTheme.typography.body1,
        modifier = Modifier.padding(bottom = 12.dp),
    )

    Divider(modifier = Modifier.padding(vertical = 8.dp))

    Text(stringResource(R.string.config_raw_label), style = MaterialTheme.typography.caption)
    Card(elevation = 2.dp) {
        Text(
            text = rawNote.take(300),
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(12.dp),
        )
    }
}

