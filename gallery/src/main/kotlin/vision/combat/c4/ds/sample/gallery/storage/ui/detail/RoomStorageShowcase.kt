package vision.combat.c4.ds.sample.gallery.storage.ui.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.storage.data.db.entity.NoteEntity
import vision.combat.c4.ds.sdk.ui.component.WindowContentDefaults.ContentPaddings
import vision.combat.c4.ds.sdk.ui.component.button.Button
import vision.combat.c4.ds.sdk.ui.component.button.OutlinedButton
import vision.combat.c4.ds.sdk.ui.component.text.AppTextFieldDefaults
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel

/**
 * Demonstrates an isolated Room database stored under the SDK-provided user directory.
 * All database operations run on [kotlinx.coroutines.Dispatchers.IO].
 */
@Composable
internal fun RoomStorageShowcase(viewModel: RoomStorageViewModel = diViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = ContentPaddings,
    ) {
        item {
            Text(
                text = stringResource(R.string.storage_room_explainer),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(bottom = 16.dp),
            )
        }

        item {
            // Add note input
            Text(
                text = stringResource(R.string.storage_room_section_add),
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            var inputText by rememberSaveable { mutableStateOf("") }
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text(stringResource(R.string.storage_room_input_hint)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = AppTextFieldDefaults.outlinedTextFieldColors(),
            )
            Button(
                label = stringResource(R.string.storage_room_add),
                onClick = {
                    viewModel.addNote(inputText)
                    inputText = ""
                },
            )
            Divider(modifier = Modifier.padding(vertical = 16.dp))
        }

        item {
            // Notes header + clear
            Text(
                text = stringResource(R.string.storage_room_section_notes),
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            OutlinedButton(
                label = stringResource(R.string.storage_room_clear),
                onClick = viewModel::clearNotes,
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))
        }

        if (uiState.notes.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.storage_room_empty),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
        } else {
            items(uiState.notes, key = { it.id }) { note ->
                NoteItem(note)
            }
        }
    }
}

@Composable
private fun NoteItem(note: NoteEntity) {
    Card(
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
    ) {
        Text(
            text = note.text,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(12.dp),
        )
    }
}
