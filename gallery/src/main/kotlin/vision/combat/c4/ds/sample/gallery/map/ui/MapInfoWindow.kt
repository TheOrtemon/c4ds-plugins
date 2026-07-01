package vision.combat.c4.ds.sample.gallery.map.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
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
 * Informational window for the Map (AbstractMapTool) sample.
 *
 * Explains the tap-to-place-placemark interaction and lists the SDK APIs demonstrated.
 * Kept entirely separate from the Renderables sample window.
 */
@Composable
internal fun MapInfoWindow() {
    WindowScaffold(
        topAppBar = {
            BackNavTopAppBar(title = stringResource(R.string.map_info_window_title))
        },
        content = { MapInfoPanel() },
    )
}

@Composable
private fun ColumnScope.MapInfoPanel() {
    Text(
        text = stringResource(R.string.map_info_explainer),
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 12.dp),
    )

    Divider()

    Text(
        text = stringResource(R.string.map_info_section_interaction),
        style = MaterialTheme.typography.subtitle2,
        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
    )
    Text(
        text = stringResource(R.string.map_info_tap_hint),
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.onSurface,
    )

    Text(
        text = stringResource(R.string.map_info_section_placemark),
        style = MaterialTheme.typography.subtitle2,
        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
    )
    Text(
        text = stringResource(R.string.map_info_placemark_hint),
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.onSurface,
    )

    Text(
        text = stringResource(R.string.map_info_section_apis),
        style = MaterialTheme.typography.subtitle2,
        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
    )
    Text(
        text = stringResource(R.string.map_info_apis_text),
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.onSurface,
    )
}
