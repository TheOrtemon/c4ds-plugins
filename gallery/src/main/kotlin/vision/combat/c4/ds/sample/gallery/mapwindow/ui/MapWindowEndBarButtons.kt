package vision.combat.c4.ds.sample.gallery.mapwindow.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.map.MapController
import vision.combat.c4.ds.sdk.ui.component.bar.endbar.EndBarActionButton
import androidx.compose.ui.res.painterResource

@Composable
internal fun MapWindowEndBarButtons(navigationController: MapController) {
    EndBarActionButton(
        icon = painterResource(R.drawable.ic_mapwindow),
        contentDescription = stringResource(R.string.mapwindow_zoom_in),
        onClick = { navigationController.zoomIn() },
    )
    EndBarActionButton(
        icon = painterResource(R.drawable.ic_mapwindow),
        contentDescription = stringResource(R.string.mapwindow_zoom_out),
        onClick = { navigationController.zoomOut() },
    )
}

