package vision.combat.c4.ds.sample.gallery.uicatalog.ui.detail

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.uicatalog.ui.list.UiCatalogEntry
import vision.combat.c4.ds.sdk.ui.component.WindowContentDefaults.ContentPaddings
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavigationButton
import vision.combat.c4.ds.sdk.ui.component.bar.TopAppBar

@Composable
internal fun UiCatalogDetailScreen(entry: UiCatalogEntry) {
    WindowScaffold(
        contentPaddingValues = if (entry != UiCatalogEntry.LISTS) ContentPaddings else PaddingValues(0.dp),
        scrollable = entry != UiCatalogEntry.LISTS,
        topAppBar = {
            TopAppBar(
                title = stringResource(entry.nameResId),
                navigationIcon = { BackNavigationButton() },
            )
        },
        content = { DetailContent(entry) },
    )
}

@Composable
private fun ColumnScope.DetailContent(entry: UiCatalogEntry) {
    if (entry != UiCatalogEntry.LISTS) {
        Text(
            text = stringResource(entry.descResId),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        Divider(modifier = Modifier.padding(bottom = 16.dp))
    }

    when (entry) {
        UiCatalogEntry.INLINE_MESSAGE -> InlineMessageDemo()
        UiCatalogEntry.HEADER_FIELD -> HeaderFieldDemo()
        UiCatalogEntry.EXPANDABLE_FIELD -> ExpandableFieldDemo()
        UiCatalogEntry.FORM_FIELD_BOX -> FormFieldBoxDemo()
        UiCatalogEntry.NESTED_FORM -> NestedFormDemo()
        UiCatalogEntry.HOSTILITY_SELECTOR -> HostilitySelectorDemo()
        UiCatalogEntry.BUTTONS -> ButtonsDemo()
        UiCatalogEntry.TOP_APP_BAR -> TopAppBarDemo()
        UiCatalogEntry.INPUTS -> InputsDemo()
        UiCatalogEntry.SELECTION -> SelectionDemo()
        UiCatalogEntry.FEEDBACK -> FeedbackDemo()
        UiCatalogEntry.LISTS -> ListsDetailScreen()
    }
}
