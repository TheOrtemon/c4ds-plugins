package vision.combat.c4.ds.sample.gallery.uicatalog.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.ui.component.bar.AppBarActionButton
import vision.combat.c4.ds.sdk.ui.component.bar.TopAppBar
import vision.combat.c4.ds.sdk.ui.component.button.AppFab
import vision.combat.c4.ds.sdk.ui.component.button.Button
import vision.combat.c4.ds.sdk.ui.component.button.DestructiveButton
import vision.combat.c4.ds.sdk.ui.component.button.OutlinedButton
import vision.combat.c4.ds.sdk.ui.component.button.PrimaryProgressButton
import vision.combat.c4.ds.sdk.ui.component.button.TextButton

@Composable
internal fun ButtonsDemo() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        DemoSection(stringResource(R.string.components_buttons)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(label = stringResource(R.string.components_btn_primary), onClick = {})
                OutlinedButton(label = stringResource(R.string.components_btn_outlined), onClick = {})
                TextButton(label = stringResource(R.string.components_btn_text), onClick = {})
                DestructiveButton(label = stringResource(R.string.components_btn_destructive), onClick = {})
                PrimaryProgressButton(
                    label = stringResource(R.string.components_btn_progress),
                    showProgress = false,
                    onClick = {},
                )
            }
        }
        DemoSection(stringResource(R.string.components_fab)) {
            AppFab(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.components_fab_cd),
                onClick = {},
            )
        }
        DemoSection(stringResource(R.string.components_top_app_bar)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.components_top_app_bar_desc),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface,
                )
                TopAppBar(
                    title = stringResource(R.string.components_top_app_bar_sample_title),
                    actions = {
                        AppBarActionButton(
                            painter = rememberVectorPainter(Icons.Default.Add),
                            label = stringResource(R.string.components_top_app_bar_action),
                            onClick = {},
                        )
                    },
                )
            }
        }
    }
}
