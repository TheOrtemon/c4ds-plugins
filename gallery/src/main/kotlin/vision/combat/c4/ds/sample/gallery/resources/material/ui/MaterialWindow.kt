/**
 * Material 2 widget isolation sample.
 *
 * This window demonstrates two strategies for popup context isolation in plugin tools:
 *
 * 1. **`ToolDropdownMenu` / `ToolAlertDialog`** (recommended) — zero-boilerplate SDK wrappers
 *    that capture and re-provide the tool's [LocalContext] inside the popup sub-composition
 *    automatically. Prefer these in real tools.
 * 2. **Raw `DropdownMenu` + explicit `ProvideWindowContext`** (intentional teaching example) —
 *    the `DropdownMenu` below is kept raw to show the underlying mechanism.  See the comment
 *    next to it and compare with the `ToolAlertDialog` usage above for the zero-boilerplate path.
 */
package vision.combat.c4.ds.sample.gallery.resources.material.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.ui.component.ProvideWindowContext
import vision.combat.c4.ds.sdk.ui.component.ToolAlertDialog
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar

@Composable
internal fun MaterialWindow() {
    WindowScaffold(
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.material_tool_name)) },
        content = { MaterialContent() },
    )
}

@Composable
private fun ColumnScope.MaterialContent() {
    // Capture the tool composition context here, before any popup sub-composition.
    // The raw DropdownMenu below re-provides it manually via ProvideWindowContext (the
    // under-the-hood mechanism). ToolAlertDialog is the SDK wrapper that does this for you.
    val windowContext = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var sliderValue by remember { mutableFloatStateOf(0.5f) }

    val snackbarMsg = stringResource(R.string.material_snackbar_message)

    Text(
        text = stringResource(R.string.material_explainer),
        style = MaterialTheme.typography.body2,
        modifier = Modifier.padding(bottom = 12.dp),
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxWidth().weight(1f, fill = false),
        backgroundColor = MaterialTheme.colors.surface,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Button(
                onClick = { scope.launch { snackbarHostState.showSnackbar(snackbarMsg) } },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            ) {
                Text(stringResource(R.string.material_show_snackbar))
            }
        }
    }

    Button(
        onClick = { showDialog = true },
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
    ) {
        Text(stringResource(R.string.material_show_dialog))
    }

    Text(stringResource(R.string.material_dropdown_label), style = MaterialTheme.typography.caption)
    Box {
        Button(onClick = { dropdownExpanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.material_dropdown_open))
        }
        // Raw Material popup — done manually to show the mechanism; prefer ToolDropdownMenu
        // (see ToolAlertDialog above) in real tools. Captures LocalContext before the popup
        // sub-composition so stringResource calls below resolve against the plugin R, not the
        // host Activity context that the popup sub-composition would otherwise inherit.
        DropdownMenu(expanded = dropdownExpanded, onDismissRequest = { dropdownExpanded = false }) {
            ProvideWindowContext(windowContext) {
                listOf(
                    stringResource(R.string.material_dropdown_alpha),
                    stringResource(R.string.material_dropdown_beta),
                    stringResource(R.string.material_dropdown_gamma),
                ).forEach { item ->
                    DropdownMenuItem(onClick = { dropdownExpanded = false }) { Text(item) }
                }
            }
        }
    }

    Text(stringResource(R.string.material_slider_label), style = MaterialTheme.typography.caption)
    Slider(
        value = sliderValue,
        onValueChange = { sliderValue = it },
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
    )

    /**
     * [ToolAlertDialog] is the zero-boilerplate path: it captures and re-provides the tool
     * [LocalContext] inside every slot lambda automatically, so plugin `stringResource` calls
     * work correctly without any manual [ProvideWindowContext] wrapping. Compare with the raw
     * [DropdownMenu] above that shows the underlying mechanism explicitly.
     *
     * For production tools that need full dialog SDK integration (dismiss callbacks, stacking),
     * prefer [vision.combat.c4.ds.sdk.tool.ToolDialog] via
     * [vision.combat.c4.ds.sdk.tool.AbstractTool.showDialog] which handles context
     * re-provision automatically.
     */
    if (showDialog) {
        ToolAlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.material_dialog_title)) },
            text = { Text(stringResource(R.string.material_dialog_body)) },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.material_ok))
                }
            },
        )
    }
}
