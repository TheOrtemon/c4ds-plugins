package vision.combat.c4.ds.sample.gallery.mapview.mapinteractor.ui.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import vision.combat.c4.ds.sample.gallery.mapview.mapinteractor.ui.list.MapInteractorShowcase
import vision.combat.c4.ds.sdk.ui.component.WindowContentDefaults.ContentPaddings
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavigationButton
import vision.combat.c4.ds.sdk.ui.component.bar.TopAppBar

/**
 * Hosts a single [MapInteractorShowcase]. Every showcase is a short scrollable form, so the
 * scaffold owns the vertical scroll for all of them.
 */
@Composable
internal fun MapInteractorShowcaseDetailScreen(showcase: MapInteractorShowcase) {
    WindowScaffold(
        scrollable = true,
        contentPaddingValues = ContentPaddings,
        topAppBar = {
            TopAppBar(
                title = stringResource(showcase.nameResId),
                navigationIcon = { BackNavigationButton() },
            )
        },
        content = {
            when (showcase) {
                MapInteractorShowcase.CAMERA_LOOKAT -> CameraLookAtShowcase()
                MapInteractorShowcase.FOCUS -> FocusShowcase()
                MapInteractorShowcase.DISPLAY_MODE -> DisplayModeShowcase()
                MapInteractorShowcase.CURSOR -> CursorShowcase()
                MapInteractorShowcase.CORRECTIONS -> CorrectionsShowcase()
            }
        },
    )
}
