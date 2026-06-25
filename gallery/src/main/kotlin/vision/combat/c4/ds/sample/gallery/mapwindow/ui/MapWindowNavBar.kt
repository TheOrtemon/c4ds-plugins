package vision.combat.c4.ds.sample.gallery.mapwindow.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.map.MapController
import vision.combat.c4.ds.sdk.ui.component.button.TextButton

@Composable
internal fun MapWindowNavBar(navigationController: MapController) {
    Row {
        TextButton(
            label = stringResource(R.string.mapwindow_mode_lookat),
            onClick = { navigationController.interactionMode = MapController.InteractionMode.LookAt },
        )
        TextButton(
            label = stringResource(R.string.mapwindow_mode_fpv),
            onClick = { navigationController.interactionMode = MapController.InteractionMode.FirstPerson },
        )
    }
}

