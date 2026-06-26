package vision.combat.c4.ds.sample.gallery.uicatalog.ui.detail

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.uicatalog.ui.list.UiCatalogEntry
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavigationButton
import vision.combat.c4.ds.sdk.ui.component.bar.TopAppBar

@Composable
internal fun UiCatalogDetailScreen(
    entry: UiCatalogEntry,
    onBack: () -> Unit,
) {
    // The LISTS demo hosts a RevealableLazyColumn (a lazy list), which must not be nested
    // inside the scaffold's default verticalScroll. Give it its own non-scrolling scaffold.
    if (entry == UiCatalogEntry.LISTS) {
        ListsDetailScreen(entry = entry, onBack = onBack)
        return
    }

    WindowScaffold(
        topAppBar = {
            TopAppBar(
                title = stringResource(entry.nameResId),
                navigationIcon = { BackNavigationButton(onBack) },
            )
        },
        content = { DetailContent(entry) },
    )
}

@Composable
private fun ColumnScope.DetailContent(entry: UiCatalogEntry) {
    Text(
        text = stringResource(entry.descResId),
        style = MaterialTheme.typography.body1,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 16.dp),
    )

    Divider(modifier = Modifier.padding(bottom = 16.dp))

    when (entry) {
        UiCatalogEntry.INLINE_MESSAGE -> InlineMessageDemo()
        UiCatalogEntry.HEADER_FIELD -> HeaderFieldDemo()
        UiCatalogEntry.EXPANDABLE_FIELD -> ExpandableFieldDemo()
        UiCatalogEntry.FORM_FIELD_BOX -> FormFieldBoxDemo()
        UiCatalogEntry.NESTED_FORM -> NestedFormDemo()
        UiCatalogEntry.HOSTILITY_SELECTOR -> HostilitySelectorDemo()
        UiCatalogEntry.BUTTONS -> ButtonsDemo()
        UiCatalogEntry.INPUTS -> InputsDemo()
        UiCatalogEntry.SELECTION -> SelectionDemo()
        UiCatalogEntry.FEEDBACK -> FeedbackDemo()
        UiCatalogEntry.LISTS -> {} // Handled separately in UiCatalogDetailScreen
    }
}
