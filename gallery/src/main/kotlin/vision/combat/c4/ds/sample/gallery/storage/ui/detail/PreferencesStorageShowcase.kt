package vision.combat.c4.ds.sample.gallery.storage.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.ui.component.button.Button
import vision.combat.c4.ds.sdk.ui.component.button.OutlinedButton
import vision.combat.c4.ds.sdk.ui.component.text.AppTextFieldDefaults
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel

/**
 * Demonstrates plugin-isolated [android.content.SharedPreferences]: puts/gets a string and
 * increments a counter, with live observation via [kotlinx.coroutines.flow.StateFlow].
 *
 * The SharedPreferences file is scoped to [vision.combat.c4.ds.sample.gallery.storage.StorageToolDescriptor]
 * so it is isolated from other tools in the host.
 */
@Composable
internal fun PreferencesStorageShowcase(viewModel: PreferencesStorageViewModel = diViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column {
        Text(
            text = stringResource(R.string.storage_prefs_explainer),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        // Current values
        Text(
            text = stringResource(R.string.storage_prefs_section_current),
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Card(elevation = 2.dp, modifier = Modifier.padding(bottom = 4.dp)) {
            Text(
                text = "${stringResource(R.string.storage_prefs_saved_string)}: \"${uiState.savedString}\"",
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(12.dp),
            )
        }
        Card(elevation = 2.dp, modifier = Modifier.padding(bottom = 16.dp)) {
            Text(
                text = "${stringResource(R.string.storage_prefs_counter)}: ${uiState.counter}",
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(12.dp),
            )
        }

        Divider(modifier = Modifier.padding(bottom = 16.dp))

        // String put
        Text(
            text = stringResource(R.string.storage_prefs_section_string),
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        var inputText by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text(stringResource(R.string.storage_prefs_input_hint)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = AppTextFieldDefaults.outlinedTextFieldColors(),
        )
        Button(
            label = stringResource(R.string.storage_prefs_save),
            onClick = {
                viewModel.saveString(inputText)
                inputText = ""
            },
        )

        Spacer(modifier = Modifier.height(16.dp))
        Divider(modifier = Modifier.padding(bottom = 16.dp))

        // Counter increment
        Text(
            text = stringResource(R.string.storage_prefs_section_counter),
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        OutlinedButton(
            label = stringResource(R.string.storage_prefs_increment),
            onClick = viewModel::increment,
        )
    }
}
