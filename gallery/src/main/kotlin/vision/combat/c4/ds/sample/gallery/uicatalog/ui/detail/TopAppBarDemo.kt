package vision.combat.c4.ds.sample.gallery.uicatalog.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.ui.component.bar.AppBarActionButton
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavigationButton
import vision.combat.c4.ds.sdk.ui.component.bar.TopAppBar
import vision.combat.c4.ds.sdk.ui.component.bar.TopAppBarTitle
import vision.combat.c4.ds.sdk.ui.component.bar.TopAppBarWithSearch
import vision.combat.c4.ds.sdk.ui.component.button.TextButton
import vision.combat.c4.ds.sdk.ui.component.menu.OverflowMenu
import vision.combat.c4.ds.sdk.ui.component.menu.OverflowMenuItem

@Composable
internal fun TopAppBarDemo() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        DemoSection(stringResource(R.string.ui_catalog_top_app_bar_state_title_only)) {
            AppBarSurface {
                TopAppBar(title = stringResource(R.string.ui_catalog_top_app_bar_title))
            }
        }
        DemoSection(stringResource(R.string.ui_catalog_top_app_bar_state_back_nav)) {
            AppBarSurface {
                TopAppBar(
                    title = stringResource(R.string.ui_catalog_top_app_bar_title),
                    navigationIcon = { BackNavigationButton {} },
                )
            }
        }
        DemoSection(stringResource(R.string.ui_catalog_top_app_bar_state_subtitle)) {
            AppBarSurface {
                BackNavTopAppBar(
                    title = stringResource(R.string.ui_catalog_top_app_bar_title),
                    subtitle = stringResource(R.string.ui_catalog_top_app_bar_subtitle),
                )
            }
        }
        DemoSection(stringResource(R.string.ui_catalog_top_app_bar_state_text_action)) {
            AppBarSurface {
                TopAppBar(
                    title = stringResource(R.string.ui_catalog_top_app_bar_title),
                    actions = {
                        TextButton(
                            label = stringResource(R.string.ui_catalog_top_app_bar_action_save),
                            onClick = {},
                        )
                    },
                )
            }
        }
        DemoSection(stringResource(R.string.ui_catalog_top_app_bar_state_icon_action)) {
            AppBarSurface {
                TopAppBar(
                    title = stringResource(R.string.ui_catalog_top_app_bar_title),
                    actions = {
                        AppBarActionButton(
                            painter = rememberVectorPainter(Icons.Default.Edit),
                            label = stringResource(R.string.ui_catalog_top_app_bar_action_edit),
                            onClick = {},
                        )
                    },
                )
            }
        }
        DemoSection(stringResource(R.string.ui_catalog_top_app_bar_state_overflow)) {
            AppBarSurface {
                TopAppBar(
                    title = stringResource(R.string.ui_catalog_top_app_bar_title),
                    actions = {
                        AppBarActionButton(
                            painter = rememberVectorPainter(Icons.Default.Add),
                            label = stringResource(R.string.ui_catalog_top_app_bar_action_add),
                            onClick = {},
                        )
                        OverflowMenu {
                            OverflowMenuItem(
                                imageVector = Icons.Default.Settings,
                                title = stringResource(R.string.ui_catalog_top_app_bar_overflow_settings),
                                onClick = {},
                            )
                            OverflowMenuItem(
                                imageVector = Icons.Default.Share,
                                title = stringResource(R.string.ui_catalog_top_app_bar_overflow_share),
                                onClick = {},
                            )
                        }
                    },
                )
            }
        }
        DemoSection(stringResource(R.string.ui_catalog_top_app_bar_state_search)) {
            AppBarSurface {
                var searchMode by remember { mutableStateOf(false) }
                var query by remember { mutableStateOf("") }
                TopAppBarWithSearch(
                    title = { TopAppBarTitle(stringResource(R.string.ui_catalog_top_app_bar_title)) },
                    navigationIcon = { BackNavigationButton {} },
                    searchMode = searchMode,
                    onSearchModeChange = { searchMode = it },
                    searchText = query,
                    onSearchTextChange = { query = it },
                    searchPlaceholder = stringResource(R.string.ui_catalog_top_app_bar_search_placeholder),
                )
            }
        }
    }
}

@Composable
private fun AppBarSurface(content: @Composable () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth()) {
        content()
    }
}
