package vision.combat.c4.ds.sample.gallery.panelstate.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
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
import vision.combat.c4.ds.sdk.ui.manager.PanelState
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel

@Composable
internal fun PanelStateWindow(viewModel: PanelStateViewModel = diViewModel()) {
    val panelState by viewModel.panelState.collectAsStateWithLifecycle()

    WindowScaffold(
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.panel_state_tool_name)) },
        content = {
            PanelStateContent(
                panelState = panelState,
                onOpenHalf = viewModel::openHalf,
                onOpenFull = viewModel::openFull,
                onClose = viewModel::close,
            )
        },
    )
}

@Composable
private fun ColumnScope.PanelStateContent(
    panelState: PanelState,
    onOpenHalf: () -> Unit,
    onOpenFull: () -> Unit,
    onClose: () -> Unit,
) {
    Text(
        text = stringResource(R.string.panel_state_explainer),
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 16.dp),
    )

    Card(elevation = 2.dp, modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = "${stringResource(R.string.panel_state_current_state)} ${panelState.toDisplayString()}",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(16.dp),
        )
    }

    Button(
        label = stringResource(R.string.panel_state_open_half),
        onClick = onOpenHalf,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Button(
        label = stringResource(R.string.panel_state_open_full),
        onClick = onOpenFull,
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedButton(
        label = stringResource(R.string.panel_state_close),
        onClick = onClose,
    )
}

@Composable
private fun PanelState.toDisplayString(): String = when (this) {
    is PanelState.Closed -> stringResource(R.string.panel_state_closed)
    is PanelState.Opened.Half -> stringResource(R.string.panel_state_half)
    is PanelState.Opened.Full -> stringResource(R.string.panel_state_full)
}
