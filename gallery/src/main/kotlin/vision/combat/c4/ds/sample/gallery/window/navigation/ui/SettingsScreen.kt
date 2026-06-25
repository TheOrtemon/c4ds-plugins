package vision.combat.c4.ds.sample.gallery.window.navigation.ui

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.ui.component.WindowContentDefaults.VerticalPadding
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel

@Composable
internal fun SettingsScreen(viewModel: SettingsViewModel = diViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    WindowScaffold(
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.window_nav_settings_title)) },
        contentPaddingValues = PaddingValues(0.dp, VerticalPadding),
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { viewModel.setOpenOnTop(!uiState.openOnTop) }
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
                        text = stringResource(R.string.window_nav_settings_open_on_top),
                        style = MaterialTheme.typography.subtitle1,
                    )
                    Text(
                        text = stringResource(R.string.window_nav_home_desc),
                        style = MaterialTheme.typography.caption,
                        color = LocalContentColor.current.copy(ContentAlpha.disabled),
                    )
                }
                Switch(
                    checked = uiState.openOnTop,
                    onCheckedChange = { viewModel.setOpenOnTop(it) },
                )
            }
        },
    )
}

