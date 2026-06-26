package vision.combat.c4.ds.sample.gallery.catalog.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.rememberInstance
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.catalog.SampleCatalog
import vision.combat.c4.ds.sample.gallery.catalog.SampleEntry
import vision.combat.c4.ds.sample.gallery.catalog.SampleSection
import vision.combat.c4.ds.sdk.tool.ToolManager
import vision.combat.c4.ds.sdk.ui.component.TextSizeIcon
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.list.ListItem

@Composable
internal fun CatalogListScreen(
    crossApkRefreshKey: Int = 0,
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
                crossApkRefreshKey = crossApkRefreshKey,
                onNavigateToDetail = onNavigateToDetail,
            )
        },
    )
}

@Composable
private fun CatalogList(
    toolManager: ToolManager,
    crossApkRefreshKey: Int,
    onNavigateToDetail: (String) -> Unit,
) {
    val entriesBySection = SampleCatalog.entries.groupBy { it.section }
    val sectionsWithEntries = SampleSection.entries.filter { entriesBySection[it]?.isNotEmpty() == true }

    // Track collapsed (not expanded) sections as a Set<String> — a plain Set is saveable via
    // the standard Saver, unlike SnapshotStateMap which has no built-in Saver.
    // All sections start expanded; toggling adds/removes the section name from the collapsed set.
    var collapsed by rememberSaveable { mutableStateOf(emptySet<String>()) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 4.dp),
    ) {
        sectionsWithEntries.forEach { section ->
            val sectionEntries = entriesBySection[section] ?: return@forEach
            val isExpanded = section.name !in collapsed

            item(key = section.name) {
                CollapsibleSectionHeader(
                    title = stringResource(section.titleResId),
                    expanded = isExpanded,
                    onToggle = {
                        collapsed = if (isExpanded) {
                            collapsed + section.name
                        } else {
                            collapsed - section.name
                        }
                    },
                )
            }

            if (isExpanded) {
                items(sectionEntries, key = { it.id }) { entry ->
                    SampleListItem(
                        entry = entry,
                        toolManager = toolManager,
                        crossApkRefreshKey = crossApkRefreshKey,
                        onDetails = { onNavigateToDetail(entry.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun CollapsibleSectionHeader(
    title: String,
    expanded: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = stringResource(
                if (expanded) R.string.catalog_section_collapse else R.string.catalog_section_expand,
            ),
            tint = MaterialTheme.colors.onSurface,
        )
    }
}

@Composable
private fun SampleListItem(
    entry: SampleEntry,
    toolManager: ToolManager,
    crossApkRefreshKey: Int,
    onDetails: () -> Unit,
) {
    val isEnabled by produceState(initialValue = !entry.isCrossApk, entry.isCrossApk, crossApkRefreshKey) {
        value = if (entry.isCrossApk) {
            entry.crossApkFqcn?.let { toolManager.resolveToolId(it) } != null
        } else {
            true
        }
    }

    val notInstalledText = if (entry.isCrossApk && !isEnabled) {
        stringResource(R.string.native_cross_apk_not_installed)
    } else {
        null
    }

    ListItem(
        headline = {
            Text(
                text = stringResource(entry.nameResId),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onSurface,
            )
        },
        supportingText = if (notInstalledText != null) {
            {
                Text(
                    text = notInstalledText,
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error,
                )
            }
        } else {
            {
                Text(
                    text = stringResource(entry.descResId),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface,
                    maxLines = 2,
                )
            }
        },
        onItemClick = if (isEnabled) {
            { entry.launch?.invoke(toolManager) }
        } else {
            null
        },
        enableClickable = isEnabled,
        canGoForward = false,
        trailingAction = {
            TextSizeIcon(
                painter = rememberVectorPainter(Icons.Outlined.Info),
                contentDescription = stringResource(R.string.catalog_details),
                onClick = onDetails,
                tint = MaterialTheme.colors.onSurface,
            )
        },
    )
}
