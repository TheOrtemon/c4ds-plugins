package vision.combat.c4.ds.sample.gallery.map.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.StateFlow
import vision.combat.c4.ds.sample.gallery.R

@Composable
internal fun MapStatusBar(lastTap: StateFlow<String?>) {
    val tap by lastTap.collectAsStateWithLifecycle()
    Text(text = if (tap != null) "${stringResource(R.string.map_last_tap)} $tap"
                else stringResource(R.string.map_status_tap_hint))
}

