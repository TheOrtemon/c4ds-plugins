package vision.combat.c4.ds.sample.gallery.dialog

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.ToolDialog
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.button.Button
import vision.combat.c4.ds.sdk.ui.component.button.DestructiveButton
import vision.combat.c4.ds.sdk.ui.component.button.TextButton

@Composable
internal fun DialogWindow(
    onShowDialog: (ToolDialog) -> Unit,
    onDismissDialog: () -> Unit,
) {
    val context = LocalContext.current

    WindowScaffold(
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.dialog_tool_name)) },
        content = {
            DialogContent(
                onShowConfirmation = {
                    onShowDialog(
                        ToolDialog.Confirmation(
                            title = context.getString(R.string.dialog_confirm_title),
                            body = context.getString(R.string.dialog_confirm_body),
                            confirmLabel = context.getString(R.string.dialog_confirm_yes),
                            dismissLabel = context.getString(R.string.dialog_confirm_cancel),
                            onConfirm = { onDismissDialog() },
                        )
                    )
                },
                onShowDestructive = {
                    onShowDialog(
                        ToolDialog.Destructive(
                            title = context.getString(R.string.dialog_destructive_title),
                            body = context.getString(R.string.dialog_destructive_body),
                            confirmLabel = context.getString(R.string.dialog_destructive_confirm),
                            dismissLabel = context.getString(R.string.dialog_confirm_cancel),
                            onConfirm = { onDismissDialog() },
                        )
                    )
                },
                onShowInfo = {
                    onShowDialog(
                        ToolDialog.Info(
                            title = context.getString(R.string.dialog_info_title),
                            body = context.getString(R.string.dialog_info_body),
                            dismissLabel = context.getString(R.string.dialog_dismiss),
                        )
                    )
                },
                onShowCustom = {
                    onShowDialog(
                        ToolDialog.Custom(
                            title = context.getString(R.string.dialog_custom_title),
                            content = { Button(label = context.getString(R.string.dialog_dismiss), onClick = { onDismissDialog() }) },
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


