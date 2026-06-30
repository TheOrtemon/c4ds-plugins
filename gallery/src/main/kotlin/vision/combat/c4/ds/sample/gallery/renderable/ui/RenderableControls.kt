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
 * Compact control panel shown in the tool's status region. Buttons add one WorldWind renderable of
 * each kind; Clear removes everything the tool has drawn. Lambdas are supplied by the owning tool.
 */
@Composable
internal fun RenderableControls(
    addedCount: StateFlow<Int>,
    onAddPoint: () -> Unit,
    onAddLine: () -> Unit,
    onAddPolygon: () -> Unit,
    onAddCircle: () -> Unit,
    onAddLabel: () -> Unit,
    onClear: () -> Unit,
) {
    val count by addedCount.collectAsStateWithLifecycle()

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = stringResource(R.string.renderable_hint),
            style = MaterialTheme.typography.caption,
        )
        Text(
            text = stringResource(R.string.renderable_count, count),
            style = MaterialTheme.typography.caption,
        )
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
