package vision.combat.c4.ds.sample.gallery.storage.ui.detail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.storage.ui.list.StorageShowcase
import vision.combat.c4.ds.sdk.ui.component.WindowContentDefaults.ContentPaddings
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavigationButton
import vision.combat.c4.ds.sdk.ui.component.bar.TopAppBar

/**
 * Hosts a single [StorageShowcase]. The Room showcase uses a LazyColumn and owns its own
 * scrolling (scaffold scroll disabled); File and Preferences are scrollable forms.
 */
@Composable
internal fun StorageShowcaseDetailScreen(showcase: StorageShowcase) {
    val ownsScroll = showcase == StorageShowcase.ROOM
    WindowScaffold(
        scrollable = !ownsScroll,
        contentPaddingValues = if (ownsScroll) PaddingValues(0.dp) else ContentPaddings,
        topAppBar = {
            TopAppBar(
                title = stringResource(showcase.nameResId),
                navigationIcon = { BackNavigationButton() },
            )
        },
        content = {
            when (showcase) {
                StorageShowcase.FILE -> FileStorageShowcase()
                StorageShowcase.PREFERENCES -> PreferencesStorageShowcase()
                StorageShowcase.ROOM -> RoomStorageShowcase()
            }
        },
    )
}
