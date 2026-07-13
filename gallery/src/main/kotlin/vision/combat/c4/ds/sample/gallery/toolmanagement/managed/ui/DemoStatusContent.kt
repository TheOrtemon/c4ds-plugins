package vision.combat.c4.ds.sample.gallery.toolmanagement.managed.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import vision.combat.c4.ds.sample.gallery.R

/**
 * The demo tool's status strip surface, proving
 * [vision.combat.c4.ds.sample.gallery.toolmanagement.managed.DemoTool]'s
 * [vision.combat.c4.ds.sdk.tool.ToolComponent.Status] can be shown/hidden independently of its
 * [vision.combat.c4.ds.sdk.tool.ToolComponent.Overlay].
 */
@Composable
internal fun DemoStatusContent() {
    Text(text = stringResource(R.string.tool_management_demo_status_label))
}
