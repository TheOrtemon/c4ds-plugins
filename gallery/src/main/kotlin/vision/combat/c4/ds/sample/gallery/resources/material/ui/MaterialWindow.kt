package vision.combat.c4.ds.sample.gallery.resources.material.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
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
import androidx.compose.runtime.CompositionLocalProvider
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
    // Capture the tool composition context here, while still under ToolEnvironmentProvider's
    // CompositionFallbackContext. Popup sub-compositions (DropdownMenu, AlertDialog) reset
    // LocalContext to the host Activity, breaking stringResource resolution against the plugin R.
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
        // Re-provide the tool context so stringResource calls below resolve against the plugin R,
        // not the host Activity context that the popup sub-composition would otherwise inherit.
        DropdownMenu(expanded = dropdownExpanded, onDismissRequest = { dropdownExpanded = false }) {
            CompositionLocalProvider(LocalContext provides windowContext) {
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
     * Both [AlertDialog] and [DropdownMenu] render in Compose popup sub-compositions where
     * [LocalContext] resets to the host Activity context, so [stringResource] calls resolve
     * against the host resource table instead of the plugin's. The fix mirrors the SDK's internal
     * `ProvideWindowContext`: capture [LocalContext] while still inside the tool's composition
     * tree (above), then re-provide it inside each popup lambda so plugin R ids resolve correctly.
     *
     * For production tools, prefer [vision.combat.c4.ds.sdk.tool.ToolDialog] via
     * [vision.combat.c4.ds.sdk.tool.AbstractTool.showDialog] which handles this automatically.
     */
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                CompositionLocalProvider(LocalContext provides windowContext) {
                    Text(stringResource(R.string.material_dialog_title))
                }
            },
            text = {
                CompositionLocalProvider(LocalContext provides windowContext) {
                    Text(stringResource(R.string.material_dialog_body))
                }
            },
            confirmButton = {
                CompositionLocalProvider(LocalContext provides windowContext) {
                    Button(onClick = { showDialog = false }) {
                        Text(stringResource(R.string.material_ok))
                    }
                }
            },
        )
    }
}
