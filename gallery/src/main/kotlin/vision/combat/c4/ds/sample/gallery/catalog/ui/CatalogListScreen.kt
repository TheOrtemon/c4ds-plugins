package vision.combat.c4.ds.sample.gallery.catalog.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import org.kodein.di.compose.rememberInstance
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.catalog.ui.CatalogListViewModel.Action
import vision.combat.c4.ds.sample.gallery.catalog.ui.CatalogListViewModel.Event
import vision.combat.c4.ds.sdk.tool.ToolManager
import vision.combat.c4.ds.sdk.ui.component.TextSizeIcon
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.AppBarActionButton
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.list.ListItem
import vision.combat.c4.ds.sdk.ui.util.showToast
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel

// ── Category List Screen (root — category cards) ──────────────────────────────

@Composable
internal fun CatalogCategoryListScreen(
    onNavigateToCategory: (CatalogSection) -> Unit,
    onNavigateToDetail: (CatalogEntry) -> Unit,
) {
    val viewModel = diViewModel<CatalogListViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val toolManager by rememberInstance<ToolManager>()

    EventHandler(events = viewModel.events)

    val entriesBySection = CatalogEntry.entries.groupBy { it.section }
    val sectionsWithEntries = CatalogSection.entries.filter { entriesBySection[it]?.isNotEmpty() == true }

    WindowScaffold(
        scrollable = false,
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
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(sectionsWithEntries, key = { it.name }) { section ->
                    val sectionEntries = entriesBySection[section].orEmpty()
                    if (sectionEntries.size == 1) {
                        val singleEntry = sectionEntries.first()
                        SingleEntrySectionCard(
                            section = section,
                            entry = singleEntry,
                            activeClassNames = uiState.activeClassNames,
                            toolManager = toolManager,
                            onAction = viewModel::handleAction,
                            onDetails = { onNavigateToDetail(singleEntry) },
                        )
                    } else {
                        SectionCard(
                            section = section,
                            onClick = { onNavigateToCategory(section) },
                        )
                    }
                }
            }
        },
    )
}

@Composable
private fun SectionCard(
    section: CatalogSection,
    onClick: () -> Unit,
) {
    ListItem(
        headline = {
            Text(
                text = stringResource(section.titleResId),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onSurface,
            )
        },
        supportingText = {
            Text(
                text = stringResource(section.descResId),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface,
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(section.iconResId),
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface,
                modifier = Modifier.size(24.dp),
            )
        },
        onItemClick = onClick,
        canGoForward = false,
        trailingAction = {
            Icon(
                painter = rememberVectorPainter(Icons.AutoMirrored.Outlined.KeyboardArrowRight),
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface,
                modifier = Modifier.size(24.dp),
            )
        },
    )
}

@Composable
private fun SingleEntrySectionCard(
    section: CatalogSection,
    entry: CatalogEntry,
    activeClassNames: Set<String>,
    toolManager: ToolManager,
    onAction: (Action) -> Unit,
    onDetails: () -> Unit,
) {
    val isActive = entry.toolClassName in activeClassNames

    val isEnabled by produceState(initialValue = !entry.isCrossApk || isActive, entry.isCrossApk, isActive) {
        value = when {
            !entry.isCrossApk -> true
            isActive -> true
            else -> toolManager.resolveToolId(entry.toolClassName) != null
        }
    }

    ListItem(
        headline = {
            Text(
                text = stringResource(section.titleResId),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.SemiBold,
                color = if (isActive) MaterialTheme.colors.secondary else MaterialTheme.colors.onSurface,
            )
        },
        supportingText = {
            Text(
                text = stringResource(section.descResId),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface,
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(section.iconResId),
                contentDescription = null,
                tint = if (isActive) MaterialTheme.colors.secondary else MaterialTheme.colors.onSurface,
                modifier = Modifier.size(24.dp),
            )
        },
        onItemClick = if (isEnabled) {
            { onAction(Action.Toggle(entry)) }
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

// ── Category Detail Screen (section subscreen — filtered sample list) ─────────

@Composable
internal fun CatalogCategoryDetailScreen(
    section: CatalogSection,
    onNavigateToDetail: (CatalogEntry) -> Unit,
) {
    val viewModel = diViewModel<CatalogListViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val toolManager by rememberInstance<ToolManager>()

    val sectionEntries = CatalogEntry.entries.filter { it.section == section }

    WindowScaffold(
        scrollable = false,
        contentPaddingValues = PaddingValues(0.dp),
        topAppBar = {
            BackNavTopAppBar(title = stringResource(section.titleResId))
        },
        content = {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(sectionEntries, key = { "entry_${it.name}" }) { entry ->
                    SampleListItem(
                        entry = entry,
                        activeClassNames = uiState.activeClassNames,
                        toolManager = toolManager,
                        onAction = viewModel::handleAction,
                        onDetails = { onNavigateToDetail(entry) },
                    )
                }
            }
        },
    )
}

// ── Shared composables ────────────────────────────────────────────────────────

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
internal fun SampleListItem(
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
        headline = {
            Text(
                text = stringResource(entry.nameResId),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.SemiBold,
                color = if (isActive) MaterialTheme.colors.secondary else MaterialTheme.colors.onSurface,
            )
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
            )
        },
        onItemClick = if (isEnabled) {
            { onAction(Action.Toggle(entry)) }
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
