package vision.combat.c4.ds.sample.gallery.model.ui.detail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.model.ui.list.ModelShowcase
import vision.combat.c4.ds.sdk.ui.component.WindowContentDefaults.ContentPaddings
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavigationButton
import vision.combat.c4.ds.sdk.ui.component.bar.TopAppBar

/**
 * Hosts a single [ModelShowcase]. The models-list showcase renders a [androidx.compose.foundation.lazy.LazyColumn]
 * and therefore owns its own scrolling (scaffold scroll disabled); the rest are short scrollable forms.
 */
@Composable
internal fun ModelShowcaseDetailScreen(showcase: ModelShowcase) {
    val ownsScroll = showcase == ModelShowcase.MODELS_LIST
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
                ModelShowcase.MODELS_LIST -> ModelsListShowcase()
                ModelShowcase.CREATE_CONSUME_COMMIT -> CreateConsumeCommitShowcase()
                ModelShowcase.SYMBOL_KEYS -> SymbolKeysShowcase()
                ModelShowcase.SELECTION_EVENTS -> SelectionEventsShowcase()
            }
        },
    )
}
