package vision.combat.c4.ds.sample.gallery.expandablestatus.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.ui.component.SegmentedButtonItem
import vision.combat.c4.ds.sdk.ui.component.SegmentedButtonRow

private enum class StatusPosition { Below, Above }

/** Expandable-status panel content with a segmented button to toggle show-above/below position. */
@Composable
internal fun ExpandableStatusContent(
    shouldShowAbove: Boolean,
    onToggleShowAbove: (Boolean) -> Unit,
) {
    val selected = if (shouldShowAbove) StatusPosition.Above else StatusPosition.Below
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = stringResource(R.string.expandable_status_position),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(bottom = 4.dp),
        )
        SegmentedButtonRow(
            items = listOf(
                SegmentedButtonItem(StatusPosition.Below, stringResource(R.string.expandable_status_pos_below)),
                SegmentedButtonItem(StatusPosition.Above, stringResource(R.string.expandable_status_pos_above)),
            ),
            selected = selected,
            onSelected = { onToggleShowAbove(it == StatusPosition.Above) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
