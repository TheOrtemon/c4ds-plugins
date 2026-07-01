package vision.combat.c4.ds.sample.gallery.renderable.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.ui.component.button.Button
import vision.combat.c4.ds.sdk.ui.component.button.OutlinedButton

/**
 * Control panel for the Renderables sample. Buttons add one WorldWind renderable of each kind;
 * Clear removes everything the tool has drawn. A color-selector row lets the user pick the outline
 * / fill color used for the next shapes. Lambdas are supplied by the owning tool.
 */
@Composable
internal fun RenderableControls(
    addedCount: StateFlow<Int>,
    selectedColor: StateFlow<RenderableColor>,
    onColorSelected: (RenderableColor) -> Unit,
    onAddPoint: () -> Unit,
    onAddLine: () -> Unit,
    onAddPolygon: () -> Unit,
    onAddCircle: () -> Unit,
    onAddLabel: () -> Unit,
    onClear: () -> Unit,
) {
    val count by addedCount.collectAsStateWithLifecycle()
    val color by selectedColor.collectAsStateWithLifecycle()

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = stringResource(R.string.renderable_hint),
            style = MaterialTheme.typography.caption,
        )
        Text(
            text = stringResource(R.string.renderable_count, count),
            style = MaterialTheme.typography.caption,
        )

        // Color chooser row
        Text(
            text = stringResource(R.string.renderable_color_label),
            style = MaterialTheme.typography.caption,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            RenderableColor.entries.forEach { c ->
                val label = stringResource(colorStringRes(c))
                if (c == color) {
                    Button(label = label, onClick = { onColorSelected(c) })
                } else {
                    OutlinedButton(label = label, onClick = { onColorSelected(c) })
                }
            }
        }

        // Shape buttons row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(label = stringResource(R.string.renderable_add_point), onClick = onAddPoint)
            Button(label = stringResource(R.string.renderable_add_line), onClick = onAddLine)
            Button(label = stringResource(R.string.renderable_add_polygon), onClick = onAddPolygon)
            Button(label = stringResource(R.string.renderable_add_circle), onClick = onAddCircle)
            Button(label = stringResource(R.string.renderable_add_label), onClick = onAddLabel)
            OutlinedButton(label = stringResource(R.string.renderable_clear), onClick = onClear, enabled = count > 0)
        }
    }
}

private fun colorStringRes(color: RenderableColor): Int = when (color) {
    RenderableColor.CYAN -> R.string.renderable_color_cyan
    RenderableColor.RED -> R.string.renderable_color_red
    RenderableColor.GREEN -> R.string.renderable_color_green
    RenderableColor.YELLOW -> R.string.renderable_color_yellow
    RenderableColor.WHITE -> R.string.renderable_color_white
}
