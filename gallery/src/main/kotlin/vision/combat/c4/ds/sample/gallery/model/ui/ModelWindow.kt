package vision.combat.c4.ds.sample.gallery.model.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.model.ModelViewModel
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.button.TextButton
import vision.combat.c4.ds.sdk.ui.component.list.ListItem
import vision.combat.c4.ds.sdk.ui.component.list.ListItemDefaults
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel
import vision.combat.c4.model.BattlespaceConceptModel

@Composable
internal fun ModelWindow(viewModel: ModelViewModel = diViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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

    Text(
        text = stringResource(R.string.model_section_selected),
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.onSurface,
    )
    Text(
        text = uiState.selectedModel?.name ?: stringResource(R.string.model_not_selected),
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 4.dp),
    )
    if (uiState.selectedModel != null) {
        TextButton(
            label = stringResource(R.string.model_unselect),
            onClick = { viewModel.unselectModel() },
        )
    }

    Divider(modifier = Modifier.padding(vertical = 8.dp))

    Text(
        text = stringResource(R.string.model_section_user),
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.onSurface,
    )
    Text(
        text = uiState.userModel?.name ?: stringResource(R.string.model_not_selected),
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 8.dp),
    )

    Divider(modifier = Modifier.padding(vertical = 8.dp))

    Text(
        text = stringResource(R.string.model_section_all),
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 8.dp),
    )
    // Non-lazy iteration inside WindowScaffold's scrollable column — a lazy list inside
    // WindowScaffold's default verticalScroll crashes with infinite-height measure.
    // The list is capped to a small preview limit so non-lazy is fine.
    if (uiState.allModels.isEmpty()) {
        Text(
            text = stringResource(R.string.model_no_models),
            color = MaterialTheme.colors.onSurface,
        )
    } else {
        uiState.allModels.forEach { model ->
            ModelRow(
                model = model,
                onSelect = { viewModel.selectModel(model) },
            )
        }
    }
}

@Composable
private fun ModelRow(
    model: BattlespaceConceptModel,
    onSelect: () -> Unit,
) {
    ListItem(
        headline = {
            Text(
                text = model.name ?: model.id.toString(),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        supportingText = {
            Text(
                text = model.id.toString(),
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.fillMaxWidth(),
            )
        },
        leadingIcon = {
            Box(modifier = Modifier.size(ListItemDefaults.LeadingIconSize)) {
                Icon(
                    painter = rememberVectorPainter(Icons.Default.Place),
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSurface,
                    modifier = Modifier.matchParentSize(),
                )
            }
        },
        onItemClick = onSelect,
        canGoForward = false,
    )
}
