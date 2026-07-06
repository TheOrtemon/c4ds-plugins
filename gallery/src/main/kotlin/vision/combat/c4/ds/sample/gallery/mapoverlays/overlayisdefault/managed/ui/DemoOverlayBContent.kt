package vision.combat.c4.ds.sample.gallery.mapoverlays.overlayisdefault.managed.ui

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
import vision.combat.c4.ds.sdk.ui.theme.primaryDarkOverlay

/**
 * [vision.combat.c4.ds.sample.gallery.mapoverlays.overlayisdefault.managed.DemoOverlayBTool]'s only
 * surface: a badge rendered top-center over the shared map, visually distinct from
 * [DemoOverlayAContent] (darker overlay tint + different text + a larger top offset) so the
 * replace-on-activate / restore-on-deactivate swap between the two default overlays is obvious.
 */
@Composable
internal fun DemoOverlayBContent() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Row(
            Modifier
                .padding(top = 56.dp)
                .background(
                    MaterialTheme.colors.primaryDarkOverlay,
                    shape = MaterialTheme.shapes.mediumOverlay,
                )
                .padding(horizontal = 16.dp, vertical = 10.dp),
        ) {
            Text(
                text = stringResource(R.string.overlay_default_b_badge),
                style = MaterialTheme.typography.subtitle1,
            )
        }
    }
}
