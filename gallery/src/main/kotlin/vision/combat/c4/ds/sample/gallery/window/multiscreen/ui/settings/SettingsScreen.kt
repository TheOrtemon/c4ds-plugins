package vision.combat.c4.ds.sample.gallery.window.multiscreen.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.window.multiscreen.ui.settings.SettingsViewModel.Action
import vision.combat.c4.ds.sample.gallery.window.multiscreen.ui.settings.SettingsViewModel.Event
import vision.combat.c4.ds.sample.gallery.window.multiscreen.ui.settings.SettingsViewModel.UiState
import vision.combat.c4.ds.sdk.ui.component.WindowContentDefaults.VerticalPadding
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.util.showToast
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel

@Composable
internal fun SettingsScreen(viewModel: SettingsViewModel = diViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Content(
        uiState = uiState,
        onAction = viewModel::handleAction,
    )

    EventHandler(events = viewModel.events)
}

@Composable
private fun Content(
    uiState: UiState,
    onAction: (Action) -> Unit,
) {
    WindowScaffold(
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.window_multi_screen_settings_title)) },
        contentPaddingValues = PaddingValues(0.dp, VerticalPadding),
        content = { SettingsContent(uiState, onAction) },
    )
}

@Composable
private fun SettingsContent(uiState: UiState, onAction: (Action) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onAction(Action.SetShowDescription(!uiState.showDescription)) }
            .heightIn(min = 44.dp)
            .padding(horizontal = 16.dp),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(R.string.window_multi_screen_settings_show_desc),
                style = MaterialTheme.typography.subtitle1,
            )
            Text(
                text = stringResource(R.string.window_multi_screen_settings_show_desc_hint),
                style = MaterialTheme.typography.caption,
                color = LocalContentColor.current.copy(ContentAlpha.disabled),
            )
        }
        Switch(
            checked = uiState.showDescription,
            onCheckedChange = { onAction(Action.SetShowDescription(it)) },
        )
    }
}

@Composable
private fun EventHandler(events: Flow<Event>) {
    val context = LocalContext.current
    val toastMessage = stringResource(R.string.window_multi_screen_settings_saved_toast)

    LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                Event.SettingSaved -> context.showToast(toastMessage)
            }
        }
    }
}
