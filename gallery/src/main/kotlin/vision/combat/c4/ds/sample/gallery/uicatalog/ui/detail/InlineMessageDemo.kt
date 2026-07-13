package vision.combat.c4.ds.sample.gallery.uicatalog.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.ui.component.button.Button
import vision.combat.c4.ds.sdk.ui.component.field.InlineMessage

@Composable
internal fun InlineMessageDemo() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        StateLabel(stringResource(R.string.ui_catalog_state_info))
        InlineMessage(InlineMessage.Info(stringResource(R.string.ui_catalog_inline_info_text)))

        StateLabel(stringResource(R.string.ui_catalog_state_error))
        InlineMessage(InlineMessage.Error(stringResource(R.string.ui_catalog_inline_error_text)))

        var visible by remember { mutableStateOf(true) }
        Button(
            label = stringResource(R.string.ui_catalog_toggle_message),
            onClick = { visible = !visible },
        )
        InlineMessage(
            if (visible) InlineMessage.Error(stringResource(R.string.ui_catalog_inline_error_text)) else null,
        )
    }
}
