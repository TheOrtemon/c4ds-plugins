package vision.combat.c4.ds.sample.gallery.uicatalog.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import vision.combat.c4.ds.sdk.ui.component.Banner
import vision.combat.c4.ds.sdk.ui.component.Carousel
import vision.combat.c4.ds.sdk.ui.component.MessageType
import vision.combat.c4.ds.sdk.ui.component.TextAction
import vision.combat.c4.ds.sdk.ui.component.Tooltip
import vision.combat.c4.ds.sdk.ui.component.button.Button
import vision.combat.c4.ds.sdk.ui.component.button.TextButton
import vision.combat.c4.ds.sdk.ui.component.dialog.AppDialog
import vision.combat.c4.ds.sdk.ui.component.dialog.ButtonsRow
import vision.combat.c4.ds.sdk.ui.component.dialog.DialogHeader

@Composable
internal fun FeedbackDemo() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        DemoSection(stringResource(R.string.components_dialog)) {
            var showDialog by remember { mutableStateOf(false) }
            Button(
                label = stringResource(R.string.components_dialog_open),
                onClick = { showDialog = true },
            )
            if (showDialog) {
                AppDialog(
                    onDismiss = { showDialog = false },
                    header = {
                        DialogHeader(
                            title = stringResource(R.string.components_dialog_title),
                            body = stringResource(R.string.components_dialog_body),
                        )
                    },
                    buttons = {
                        ButtonsRow {
                            TextButton(
                                label = stringResource(R.string.components_dialog_close),
                                onClick = { showDialog = false },
                            )
                        }
                    },
                )
            }
        }
        DemoSection(stringResource(R.string.components_banner)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Banner(type = MessageType.Info, message = stringResource(R.string.components_banner_info))
                Banner(type = MessageType.Warning, message = stringResource(R.string.components_banner_warning))
                Banner(
                    type = MessageType.Error,
                    message = stringResource(R.string.components_banner_error),
                    action = TextAction(label = stringResource(R.string.components_banner_action)) {},
                )
            }
        }
        DemoSection(stringResource(R.string.components_carousel)) {
            val items = listOf(
                stringResource(R.string.components_carousel_item_1),
                stringResource(R.string.components_carousel_item_2),
                stringResource(R.string.components_carousel_item_3),
            )
            Carousel(data = items) { item ->
                Text(
                    text = item,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
        }
        DemoSection(stringResource(R.string.components_tooltip)) {
            val expanded = remember { mutableStateOf(false) }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.components_tooltip_desc),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface,
                )
                Box {
                    Tooltip(expanded = expanded) {
                        Text(
                            text = stringResource(R.string.components_tooltip_content),
                            modifier = Modifier.padding(8.dp),
                        )
                    }
                    Button(
                        label = stringResource(R.string.components_tooltip_show),
                        onClick = { expanded.value = !expanded.value },
                    )
                }
            }
        }
    }
}
