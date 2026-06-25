package vision.combat.c4.ds.sample.gallery.catalog.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.catalog.SampleCatalog
import vision.combat.c4.ds.sample.gallery.catalog.SampleEntry
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavigationButton
import vision.combat.c4.ds.sdk.ui.component.bar.TopAppBar

@Composable
internal fun CatalogDetailScreen(
    sampleId: String,
    onBack: () -> Unit,
) {
    val entry = SampleCatalog.entries.firstOrNull { it.id == sampleId } ?: run {
        onBack()
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
private fun ColumnScope.DetailContent(entry: SampleEntry) {
    Text(
        text = stringResource(entry.descResId),
        style = MaterialTheme.typography.body1,
        modifier = Modifier.padding(bottom = 16.dp),
    )

    Divider(modifier = Modifier.padding(bottom = 12.dp))

    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(
            text = stringResource(R.string.catalog_sdk_apis_label),
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp),
        )
        Text(
            text = stringResource(entry.apisResId),
            style = MaterialTheme.typography.body2,
        )
    }

    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(
            text = stringResource(R.string.catalog_source_label),
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp),
        )
        Text(
            text = entry.sourceSubpackage,
            style = MaterialTheme.typography.body2,
            fontFamily = FontFamily.Monospace,
        )
    }
}

