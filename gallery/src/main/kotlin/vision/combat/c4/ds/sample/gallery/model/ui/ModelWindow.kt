package vision.combat.c4.ds.sample.gallery.model.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.model.ModelViewModel
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.button.Button
import vision.combat.c4.ds.sdk.ui.component.button.DestructiveButton
import vision.combat.c4.ds.sdk.ui.component.button.TextButton
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel
import vision.combat.c4.model.BattlespaceConceptModel

@Composable
internal fun ModelWindow(viewModel: ModelViewModel = diViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    WindowScaffold(
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.model_tool_name)) },
        content = { ModelContent(uiState, viewModel) },
    )
}

@Composable
private fun ColumnScope.ModelContent(uiState: ModelViewModel.UiState, viewModel: ModelViewModel) {
    if (uiState.isReadOnly) {
        Text(
            text = stringResource(R.string.model_read_only_warning),
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.error,
            modifier = Modifier.padding(bottom = 8.dp),
        )
    }

    Text(stringResource(R.string.model_section_selected), style = MaterialTheme.typography.h6)
    Text(
        text = uiState.selectedModel?.name ?: stringResource(R.string.model_not_selected),
        modifier = Modifier.padding(bottom = 4.dp),
    )
    if (uiState.selectedModel != null) {
        TextButton(
            label = stringResource(R.string.model_unselect),
            onClick = { viewModel.unselectModel() },
        )
    }

    Divider(modifier = Modifier.padding(vertical = 8.dp))

    Text(stringResource(R.string.model_section_user), style = MaterialTheme.typography.h6)
    Text(
        text = uiState.userModel?.name ?: stringResource(R.string.model_not_selected),
        modifier = Modifier.padding(bottom = 8.dp),
    )

    Divider(modifier = Modifier.padding(vertical = 8.dp))

    Row {
        Text(
            text = stringResource(R.string.model_section_all),
            style = MaterialTheme.typography.h6,
            modifier = Modifier.weight(1f),
        )
        Button(
            label = stringResource(R.string.model_create),
            onClick = { viewModel.createModel() },
            enabled = !uiState.isReadOnly,
        )
    }

    if (uiState.allModels.isEmpty()) {
        Text(stringResource(R.string.model_no_models))
    } else {
        LazyColumn {
            items(uiState.allModels) { model ->
                ModelRow(
                    model = model,
                    isReadOnly = uiState.isReadOnly,
                    onSelect = { viewModel.selectModel(model.id) },
                    onDelete = { viewModel.deleteModel(model.id) },
                )
            }
        }
    }
}

@Composable
private fun ModelRow(
    model: BattlespaceConceptModel,
    isReadOnly: Boolean,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(
        elevation = 1.dp,
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Text(
                text = model.name ?: model.id.toString(),
                modifier = Modifier.weight(1f),
            )
            TextButton(label = stringResource(R.string.model_select), onClick = onSelect)
            if (!isReadOnly) {
                DestructiveButton(label = stringResource(R.string.model_delete), onClick = onDelete)
            }
        }
    }
}

