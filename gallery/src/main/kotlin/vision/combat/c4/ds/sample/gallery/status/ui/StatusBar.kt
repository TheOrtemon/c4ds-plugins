package vision.combat.c4.ds.sample.gallery.status.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import org.kodein.di.compose.rememberInstance
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.domain.interactor.CommonMapInteractor

@Composable
internal fun StatusBar() {
    val mapInteractor by rememberInstance<CommonMapInteractor>()
    val position by mapInteractor.selectedPosition.collectAsState(initial = null)

    Text(
        text = position?.let { "${stringResource(R.string.status_coords_label)} ${it.latitude.degrees}°, ${it.longitude.degrees}°" }
            ?: stringResource(R.string.status_coords_label),
    )
}

