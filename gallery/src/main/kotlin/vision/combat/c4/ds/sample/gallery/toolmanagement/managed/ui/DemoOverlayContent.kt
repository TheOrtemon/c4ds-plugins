package vision.combat.c4.ds.sample.gallery.toolmanagement.managed.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.ui.theme.mediumOverlay
import vision.combat.c4.ds.sdk.ui.theme.primaryOverlay

/**
 * The demo tool's only surface: a small badge rendered top-center over the shared map, proving
 * [vision.combat.c4.ds.sample.gallery.toolmanagement.managed.DemoTool] is active without opening
 * any panel window.
 */
@Composable
internal fun DemoOverlayContent() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Row(
            Modifier
                .padding(top = 24.dp)
                .background(
                    MaterialTheme.colors.primaryOverlay,
                    shape = MaterialTheme.shapes.mediumOverlay,
                )
                .padding(horizontal = 16.dp, vertical = 10.dp),
        ) {
            Text(
                text = stringResource(R.string.tool_management_demo_overlay_badge),
                style = MaterialTheme.typography.subtitle1,
            )
        }
    }
}
