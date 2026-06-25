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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var sliderValue by remember { mutableFloatStateOf(0.5f) }

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
                onClick = {
                    scope.launch { snackbarHostState.showSnackbar("Snackbar from plugin — host R resolved OK") }
                },
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
            Text("Open DropdownMenu")
        }
        DropdownMenu(expanded = dropdownExpanded, onDismissRequest = { dropdownExpanded = false }) {
            listOf("Alpha", "Beta", "Gamma").forEach { item ->
                DropdownMenuItem(onClick = { dropdownExpanded = false }) { Text(item) }
            }
        }
    }

    Text(stringResource(R.string.material_slider_label), style = MaterialTheme.typography.caption)
    Slider(
        value = sliderValue,
        onValueChange = { sliderValue = it },
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.material_dialog_title)) },
            text = { Text(stringResource(R.string.material_dialog_body)) },
            confirmButton = { Button(onClick = { showDialog = false }) { Text("OK") } },
        )
    }
}

