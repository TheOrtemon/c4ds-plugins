package vision.combat.c4.ds.sample.gallery.mapoverlays.overlayisdefault.ui

import androidx.compose.foundation.layout.ColumnScope
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
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.button.Button
import vision.combat.c4.ds.sdk.ui.component.button.OutlinedButton
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel

@Composable
internal fun OverlayDefaultWindow(viewModel: OverlayDefaultViewModel = diViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WindowScaffold(
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.overlay_default_tool_name)) },
        content = { OverlayDefaultContent(uiState, viewModel) },
    )
}

@Composable
private fun ColumnScope.OverlayDefaultContent(
    uiState: OverlayDefaultViewModel.UiState,
    viewModel: OverlayDefaultViewModel,
) {
    Text(
        text = stringResource(R.string.overlay_default_explainer),
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 16.dp),
    )

    Text(
        text = stringResource(R.string.overlay_default_a_section),
        style = MaterialTheme.typography.subtitle1,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 8.dp),
    )

    Button(
        label = stringResource(R.string.overlay_default_activate_a),
        onClick = viewModel::activateDemoA,
        enabled = !uiState.isDemoAActive,
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedButton(
        label = stringResource(R.string.overlay_default_deactivate_a),
        onClick = viewModel::deactivateDemoA,
        enabled = uiState.isDemoAActive,
    )

    Divider(modifier = Modifier.padding(vertical = 16.dp))

    Text(
        text = stringResource(R.string.overlay_default_b_section),
        style = MaterialTheme.typography.subtitle1,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 8.dp),
    )

    Button(
        label = stringResource(R.string.overlay_default_activate_b),
        onClick = viewModel::activateDemoB,
        enabled = !uiState.isDemoBActive,
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedButton(
        label = stringResource(R.string.overlay_default_deactivate_b),
        onClick = viewModel::deactivateDemoB,
        enabled = uiState.isDemoBActive,
    )

    Spacer(modifier = Modifier.height(12.dp))

    Card(elevation = 1.dp, modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            text = stringResource(R.string.overlay_default_watch_hint),
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(12.dp),
        )
    }
}
