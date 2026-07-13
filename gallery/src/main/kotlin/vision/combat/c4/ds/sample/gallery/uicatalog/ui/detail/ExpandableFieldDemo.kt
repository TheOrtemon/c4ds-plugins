package vision.combat.c4.ds.sample.gallery.uicatalog.ui.detail

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.ui.component.field.ExpandableField

@Composable
internal fun ExpandableFieldDemo() {
    var expanded by remember { mutableStateOf(false) }
    ExpandableField(
        label = stringResource(R.string.ui_catalog_expandable_label),
        isExpanded = expanded,
        onExpandChange = { expanded = !expanded },
        horizontalPadding = 0.dp,
    ) {
        Text(
            text = stringResource(R.string.ui_catalog_expandable_content),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(16.dp),
        )
    }
}
