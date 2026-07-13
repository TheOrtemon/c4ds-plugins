package vision.combat.c4.ds.sample.gallery.uicatalog.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.ui.component.field.HeaderField

@Composable
internal fun HeaderFieldDemo() {
    var clicks by remember { mutableIntStateOf(0) }
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        StateLabel(stringResource(R.string.ui_catalog_state_default))
        HeaderField(
            label = stringResource(R.string.ui_catalog_header_label),
            horizontalPadding = 0.dp,
            onClick = { clicks++ },
            trailingIcon = { Icon(Icons.AutoMirrored.Filled.ArrowRight, contentDescription = null) },
        )
        Text(
            text = stringResource(R.string.ui_catalog_header_clicks, clicks),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface,
        )

        StateLabel(stringResource(R.string.ui_catalog_header_label_custom_color))
        HeaderField(
            label = stringResource(R.string.ui_catalog_header_label_custom_color),
            horizontalPadding = 0.dp,
            backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.15f),
            onClick = {},
            trailingIcon = { Icon(Icons.AutoMirrored.Filled.ArrowRight, contentDescription = null) },
        )

        StateLabel(stringResource(R.string.ui_catalog_header_label_default_shape))
        HeaderField(
            label = stringResource(R.string.ui_catalog_header_label_default_shape),
            horizontalPadding = 0.dp,
            shape = MaterialTheme.shapes.large,
            onClick = {},
            trailingIcon = { Icon(Icons.AutoMirrored.Filled.ArrowRight, contentDescription = null) },
        )
    }
}
