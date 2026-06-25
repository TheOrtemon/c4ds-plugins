package vision.combat.c4.ds.sample.gallery.dialog

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.ToolDialog
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.button.Button
import vision.combat.c4.ds.sdk.ui.component.button.DestructiveButton
import vision.combat.c4.ds.sdk.ui.component.button.TextButton
import vision.combat.c4.ds.sdk.ui.component.dialog.ButtonsRow
import vision.combat.c4.ds.sdk.ui.component.dialog.DialogHeader

@Composable
internal fun DialogWindow(
    onShowDialog: (ToolDialog) -> Unit,
    onDismissDialog: () -> Unit,
) {
    val confirmTitle = stringResource(R.string.dialog_confirm_title)
    val confirmBody = stringResource(R.string.dialog_confirm_body)
    val confirmYes = stringResource(R.string.dialog_confirm_yes)
    val confirmCancel = stringResource(R.string.dialog_confirm_cancel)
    val destructiveTitle = stringResource(R.string.dialog_destructive_title)
    val destructiveBody = stringResource(R.string.dialog_destructive_body)
    val destructiveConfirm = stringResource(R.string.dialog_destructive_confirm)
    val infoTitle = stringResource(R.string.dialog_info_title)
    val infoBody = stringResource(R.string.dialog_info_body)
    val dismiss = stringResource(R.string.dialog_dismiss)
    val customTitle = stringResource(R.string.dialog_custom_title)
    val customBody = stringResource(R.string.dialog_custom_body)

    WindowScaffold(
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.dialog_tool_name)) },
        content = {
            DialogContent(
                onShowConfirmation = {
                    onShowDialog(
                        ToolDialog.Confirmation(
                            title = confirmTitle,
                            body = confirmBody,
                            confirmLabel = confirmYes,
                            dismissLabel = confirmCancel,
                            onConfirm = { onDismissDialog() },
                        )
                    )
                },
                onShowDestructive = {
                    onShowDialog(
                        ToolDialog.Destructive(
                            title = destructiveTitle,
                            body = destructiveBody,
                            confirmLabel = destructiveConfirm,
                            dismissLabel = confirmCancel,
                            onConfirm = { onDismissDialog() },
                        )
                    )
                },
                onShowInfo = {
                    onShowDialog(
                        ToolDialog.Info(
                            title = infoTitle,
                            body = infoBody,
                            dismissLabel = dismiss,
                        )
                    )
                },
                onShowCustom = {
                    onShowDialog(
                        ToolDialog.Custom(
                            // DialogHeaderScope only exposes DialogHeader(icon?, title, body?)
                            header = {
                                DialogHeader(title = customTitle)
                            },
                            content = {
                                Text(
                                    text = customBody,
                                    modifier = Modifier.padding(horizontal = 24.dp),
                                )
                            },
                            // DialogButtonsScope exposes ButtonsRow/ButtonsColumn
                            buttons = {
                                ButtonsRow {
                                    TextButton(
                                        label = dismiss,
                                        onClick = { onDismissDialog() },
                                    )
                                }
                            },
                        )
                    )
                },
            )
        },
    )
}

@Composable
private fun ColumnScope.DialogContent(
    onShowConfirmation: () -> Unit,
    onShowDestructive: () -> Unit,
    onShowInfo: () -> Unit,
    onShowCustom: () -> Unit,
) {
    Text(
        text = stringResource(R.string.dialog_desc),
        style = MaterialTheme.typography.body2,
        modifier = Modifier.padding(bottom = 16.dp),
    )
    Button(label = stringResource(R.string.dialog_show_confirmation), onClick = onShowConfirmation)
    DestructiveButton(label = stringResource(R.string.dialog_show_destructive), onClick = onShowDestructive)
    Button(label = stringResource(R.string.dialog_show_info), onClick = onShowInfo)
    TextButton(label = stringResource(R.string.dialog_show_custom), onClick = onShowCustom)
}


