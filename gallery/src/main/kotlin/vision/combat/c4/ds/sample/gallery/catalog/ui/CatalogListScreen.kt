package vision.combat.c4.ds.sample.gallery.catalog.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.rememberInstance
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.catalog.SampleCatalog
import vision.combat.c4.ds.sample.gallery.catalog.SampleEntry
import vision.combat.c4.ds.sample.gallery.catalog.SampleSection
import vision.combat.c4.ds.sdk.tool.ToolManager
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.button.Button
import vision.combat.c4.ds.sdk.ui.component.button.TextButton

@Composable
internal fun CatalogListScreen(
    onNavigateToDetail: (String) -> Unit,
) {
    val toolManager by rememberInstance<ToolManager>()

    WindowScaffold(
        // The catalog body is a LazyColumn, which must own its own scrolling — disable the
        // scaffold's default verticalScroll wrapper to avoid nesting a lazy list inside a
        // vertically scrollable parent (infinite-height measure crash).
        scrollable = false,
        // Let the LazyColumn own all padding so top/bottom breathing room scrolls with content.
        contentPaddingValues = PaddingValues(0.dp),
        topAppBar = {
            BackNavTopAppBar(title = stringResource(R.string.catalog_tool_name))
        },
        content = {
            CatalogList(
                toolManager = toolManager,
                onNavigateToDetail = onNavigateToDetail,
            )
        },
    )
}

@Composable
private fun CatalogList(
    toolManager: ToolManager,
    onNavigateToDetail: (String) -> Unit,
) {
    val entriesBySection = SampleCatalog.entries.groupBy { it.section }
    val sectionsWithEntries = SampleSection.entries.filter { entriesBySection[it]?.isNotEmpty() == true }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 12.dp),
    ) {
        sectionsWithEntries.forEachIndexed { index, section ->
            val sectionEntries = entriesBySection[section] ?: return@forEachIndexed

            item(key = section.name) {
                SectionHeader(section)
            }

            items(sectionEntries, key = { it.id }) { entry ->
                SampleCard(
                    entry = entry,
                    toolManager = toolManager,
                    onDetails = { onNavigateToDetail(entry.id) },
                )
            }

            if (index < sectionsWithEntries.lastIndex) {
                item(key = "${section.name}_divider") {
                    Divider(modifier = Modifier.padding(vertical = 12.dp))
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(section: SampleSection) {
    Text(
        text = stringResource(section.titleResId),
        style = MaterialTheme.typography.subtitle1,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = MaterialTheme.colors.onSurface,
    )
}

@Composable
private fun SampleCard(
    entry: SampleEntry,
    toolManager: ToolManager,
    onDetails: () -> Unit,
) {
    // For cross-APK entries, check if the target is installed on each recomposition
    val isEnabled by produceState(initialValue = !entry.isCrossApk, entry.isCrossApk) {
        value = if (entry.isCrossApk) {
            entry.crossApkFqcn?.let { toolManager.resolveToolId(it) } != null
        } else {
            true
        }
    }

    Card(
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = stringResource(entry.nameResId),
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = stringResource(entry.descResId),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
            )
            if (entry.isCrossApk && !isEnabled) {
                Text(
                    text = stringResource(R.string.native_cross_apk_not_installed),
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    label = stringResource(R.string.catalog_launch),
                    onClick = { entry.launch?.invoke(toolManager) },
                    enabled = isEnabled,
                )
                TextButton(
                    label = stringResource(R.string.catalog_details),
                    onClick = onDetails,
                )
            }
        }
    }
}

