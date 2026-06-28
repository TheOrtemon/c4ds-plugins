package vision.combat.c4.ds.sample.gallery.catalog.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import org.kodein.di.compose.rememberInstance
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.catalog.ui.CatalogListViewModel.Action
import vision.combat.c4.ds.sample.gallery.catalog.ui.CatalogListViewModel.Event
import vision.combat.c4.ds.sample.gallery.catalog.ui.CatalogListViewModel.UiState
import vision.combat.c4.ds.sdk.tool.ToolManager
import vision.combat.c4.ds.sdk.ui.component.TextSizeIcon
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.AppBarActionButton
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.list.ListItem
import vision.combat.c4.ds.sdk.ui.util.showToast
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel

@Composable
internal fun CatalogListScreen(
    onNavigateToDetail: (CatalogEntry) -> Unit,
) {
    val viewModel = diViewModel<CatalogListViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val toolManager by rememberInstance<ToolManager>()

    EventHandler(events = viewModel.events)

    WindowScaffold(
        // The catalog body is a LazyColumn, which must own its own scrolling — disable the
        // scaffold's default verticalScroll wrapper to avoid nesting a lazy list inside a
        // vertically scrollable parent (infinite-height measure crash).
        scrollable = false,
        // Let the LazyColumn own all padding so top/bottom breathing room scrolls with content.
        contentPaddingValues = PaddingValues(0.dp),
        topAppBar = {
            BackNavTopAppBar(
                title = stringResource(R.string.catalog_tool_name),
                actions = {
                    if (uiState.canDeactivateAll) {
                        AppBarActionButton(
                            painter = rememberVectorPainter(Icons.Default.Close),
                            label = stringResource(R.string.catalog_deactivate_all),
                            onClick = { viewModel.handleAction(Action.DeactivateAll) },
                        )
                    }
                },
            )
        },
        content = {
            CatalogList(
                uiState = uiState,
                onAction = viewModel::handleAction,
                onNavigateToDetail = onNavigateToDetail,
                toolManager = toolManager,
            )
        },
    )
}

@Composable
private fun EventHandler(events: Flow<Event>) {
    val context = LocalContext.current
    val deactivatedAllMessage = stringResource(R.string.catalog_deactivated_all_toast)
    LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                is Event.AllDeactivated -> context.showToast(deactivatedAllMessage)
            }
        }
    }
}

@Composable
private fun CatalogList(
    uiState: UiState,
    onAction: (Action) -> Unit,
    onNavigateToDetail: (CatalogEntry) -> Unit,
    toolManager: ToolManager,
) {
    val entriesBySection = CatalogEntry.entries.groupBy { it.section }
    val sectionsWithEntries = CatalogSection.entries.filter { entriesBySection[it]?.isNotEmpty() == true }

    // Track collapsed (not expanded) sections as a Set<String> — a plain Set is saveable via
    // the standard Saver, unlike SnapshotStateMap which has no built-in Saver.
    // All sections start expanded; toggling adds/removes the section name from the collapsed set.
    var collapsed by rememberSaveable { mutableStateOf(emptySet<String>()) }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        sectionsWithEntries.forEach { section ->
            val sectionEntries = entriesBySection[section] ?: return@forEach
            val isExpanded = section.name !in collapsed

            item(key = "section_${section.name}") {
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
                items(sectionEntries, key = { "entry_${it.name}" }) { entry ->
                    SampleListItem(
                        entry = entry,
                        activeClassNames = uiState.activeClassNames,
                        toolManager = toolManager,
                        onAction = onAction,
                        onDetails = { onNavigateToDetail(entry) },
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
    entry: CatalogEntry,
    activeClassNames: Set<String>,
    toolManager: ToolManager,
    onAction: (Action) -> Unit,
    onDetails: () -> Unit,
) {
    val isActive = entry.toolClassName in activeClassNames

    // Cross-APK entries require the isolation APK to be installed to be enabled.
    // If the entry is already active the tool is live so we skip the resolveToolId check.
    val isEnabled by produceState(initialValue = !entry.isCrossApk || isActive, entry.isCrossApk, isActive) {
        value = when {
            !entry.isCrossApk -> true
            isActive -> true
            else -> toolManager.resolveToolId(entry.toolClassName) != null
        }
    }

    val notInstalledText = if (entry.isCrossApk && !isEnabled) {
        stringResource(R.string.native_cross_apk_not_installed)
    } else {
        null
    }

    ListItem(
        selected = isActive,
        // The details (info) affordance lives in the headline row so the supporting text below
        // never changes between states — this keeps the row height stable and the list from
        // re-laying-out (jumping) when a sample is toggled active/inactive.
        headline = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(entry.nameResId),
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.weight(1f, fill = false),
                )
                TextSizeIcon(
                    painter = rememberVectorPainter(Icons.Outlined.Info),
                    contentDescription = stringResource(R.string.catalog_details),
                    onClick = onDetails,
                    tint = MaterialTheme.colors.onSurface,
                )
            }
        },
        supportingText = {
            Text(
                text = notInstalledText ?: stringResource(entry.descResId),
                style = if (notInstalledText != null) {
                    MaterialTheme.typography.caption
                } else {
                    MaterialTheme.typography.body2
                },
                color = if (notInstalledText != null) {
                    MaterialTheme.colors.error
                } else {
                    MaterialTheme.colors.onSurface
                },
                maxLines = 2,
            )
        },
        onItemClick = if (isEnabled) {
            { onAction(Action.Toggle(entry)) }
        } else {
            null
        },
        enableClickable = isEnabled,
        canGoForward = false,
        // A check toggle replaces the old info button as the active-state control: its checked
        // state mirrors whether the sample's tool is active, and tapping it (or the row) toggles it.
        trailingAction = if (isEnabled) {
            {
                Checkbox(
                    checked = isActive,
                    onCheckedChange = { onAction(Action.Toggle(entry)) },
                )
            }
        } else {
            null
        },
    )
}
