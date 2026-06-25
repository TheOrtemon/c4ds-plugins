package vision.combat.c4.ds.sample.gallery.expandablestatus.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R

@Composable
internal fun ExpandableStatusContent(
    isExpanded: Boolean,
    shouldShowAbove: Boolean,
    onToggleShowAbove: (Boolean) -> Unit,
) {
    if (isExpanded) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(
                text = stringResource(R.string.expandable_status_position),
                style = MaterialTheme.typography.body2,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.expandable_status_show_above),
                    modifier = Modifier.weight(1f),
                )
                Switch(
                    checked = shouldShowAbove,
                    onCheckedChange = onToggleShowAbove,
                )
            }
        }
    } else {
        Text(
            text = stringResource(R.string.expandable_status_expand),
            style = MaterialTheme.typography.caption,
        )
    }
}

