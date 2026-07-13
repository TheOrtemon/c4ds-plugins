package vision.combat.c4.ds.sample.gallery.storage.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.storage.ui.StorageViewModel
import vision.combat.c4.ds.sdk.ui.component.button.Button
import vision.combat.c4.ds.sdk.ui.component.button.OutlinedButton
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel

/**
 * Demonstrates [vision.combat.c4.ds.sdk.domain.interactor.CommonSessionStorageInteractor]:
 * shows root/user directory paths, writes a sample file off the main thread and reads it back.
 */
@Composable
internal fun FileStorageShowcase(viewModel: StorageViewModel = diViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column {
        Text(
            text = stringResource(R.string.storage_explainer),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        // Directory paths
        Text(
            text = stringResource(R.string.storage_section_paths),
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Card(elevation = 2.dp, modifier = Modifier.padding(bottom = 4.dp)) {
            Text(
                text = "${stringResource(R.string.storage_root_dir)}\n${uiState.rootDirectoryPath}",
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(12.dp),
            )
        }
        Card(elevation = 2.dp, modifier = Modifier.padding(bottom = 16.dp)) {
            Text(
                text = "${stringResource(R.string.storage_user_dir)}\n${uiState.userDirectoryPath}",
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(12.dp),
            )
        }

        Divider(modifier = Modifier.padding(bottom = 16.dp))

        // File operations
        Text(
            text = stringResource(R.string.storage_section_file_ops),
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        Button(
            label = stringResource(R.string.storage_write),
            onClick = viewModel::writeFile,
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            label = stringResource(R.string.storage_read),
            onClick = viewModel::readFile,
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Status
        val statusText = when (val status = uiState.fileOperationStatus) {
            StorageViewModel.FileOperationStatus.Idle -> ""
            StorageViewModel.FileOperationStatus.InProgress -> stringResource(R.string.storage_status_in_progress)
            StorageViewModel.FileOperationStatus.WriteSuccess -> stringResource(R.string.storage_status_write_success)
            StorageViewModel.FileOperationStatus.ReadSuccess -> stringResource(R.string.storage_status_read_success)
            StorageViewModel.FileOperationStatus.FileNotFound -> stringResource(R.string.storage_status_file_not_found)
            is StorageViewModel.FileOperationStatus.Error ->
                "${stringResource(R.string.storage_status_error)} ${status.message ?: stringResource(R.string.storage_error_unknown)}"
        }
        if (statusText.isNotEmpty()) {
            Card(elevation = 2.dp, modifier = Modifier.padding(bottom = 8.dp)) {
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(12.dp),
                )
            }
        }

        uiState.readContent?.let { content ->
            if (uiState.fileOperationStatus == StorageViewModel.FileOperationStatus.ReadSuccess) {
                Card(elevation = 2.dp) {
                    Text(
                        text = "${stringResource(R.string.storage_file_content)}\n$content",
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(12.dp),
                    )
                }
            }
        }
    }
}
