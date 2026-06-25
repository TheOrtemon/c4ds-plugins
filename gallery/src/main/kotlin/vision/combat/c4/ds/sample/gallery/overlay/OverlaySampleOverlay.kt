package vision.combat.c4.ds.sample.gallery.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.kodein.di.compose.rememberInstance
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.ToolManager
import vision.combat.c4.ds.sdk.tool.deactivate
import vision.combat.c4.ds.sdk.ui.theme.mediumOverlay
import vision.combat.c4.ds.sdk.ui.theme.primaryOverlay
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel

@Composable
internal fun OverlaySampleOverlay(viewModel: OverlaySampleViewModel = diViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val toolManager by rememberInstance<ToolManager>()

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            Modifier
                .background(
                    MaterialTheme.colors.primaryOverlay,
                    shape = MaterialTheme.shapes.mediumOverlay,
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = stringResource(R.string.overlay_tool_name),
                style = MaterialTheme.typography.h6,
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(R.string.overlay_cursor_position))
                Text(uiState.selectedPosition ?: stringResource(R.string.overlay_not_available))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(R.string.overlay_user_model))
                Text(uiState.userModel ?: stringResource(R.string.overlay_not_available))
            }
            TextButton(onClick = { toolManager.deactivate<OverlaySampleToolDescriptor>() }) {
                Text(stringResource(R.string.overlay_close))
            }
        }
    }
}

