package vision.combat.c4.ds.sample.gallery.expandablestatus

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.expandablestatus.ui.ExpandableStatusContent
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.endBar
import vision.combat.c4.ds.sdk.tool.expandableStatusComponent
import vision.combat.c4.ds.sdk.ui.component.bar.endbar.EndBarToggleButton

/** Minimal [AbstractTool] subclass wiring [ExpandableStatusContent] + an end-bar toggle button. */
internal class ExpandableStatusTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val expandableStatus: ToolComponent.ExpandableStatus by expandableStatusComponent(
        isDefault = true,
        isExpanded = false,
        shouldShowAbove = false,
    ) {
        ExpandableStatusContent(
            shouldShowAbove = shouldShowAbove,
            onToggleShowAbove = { shouldShowAbove = it },
        )
    }

    /**
     * Programmatic demo of [ToolComponent.ExpandableStatus.isExpanded]; duplicates the host
     * chevron above the status bar that toggles expand/collapse for the same component.
     */
    override val endBar by endBar {
        EndBarToggleButton(
            icon = painterResource(R.drawable.ic_expandable_status),
            contentDescription = stringResource(R.string.expandable_status_toggle_cd),
            isChecked = expandableStatus.isExpanded,
            onCheckedChange = { expandableStatus.isExpanded = it },
        )
    }
}
