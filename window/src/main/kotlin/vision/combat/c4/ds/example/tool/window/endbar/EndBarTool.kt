package vision.combat.c4.ds.example.tool.window.endbar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.kodein.di.DI
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.endBar
import vision.combat.c4.ds.sdk.tool.requiredComponent
import vision.combat.c4.ds.sdk.ui.component.bar.endbar.EndBarActionButton
import vision.combat.c4.ds.sdk.ui.component.bar.endbar.EndBarToggleButton
import vision.combat.c4.ds.tool.sample.window.R

/**
 * Case (g): EndBar Painter API — EndBarActionButton and EndBarToggleButton each receive
 * a painterResource(R.drawable.ic_endbar) sourced from the plugin APK.
 * This verifies that painterResource inside endBar {} resolves against the plugin's resources.
 */
internal class EndBarTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    private var isToggled by mutableStateOf(false)

    override val window: ToolComponent.Window by requiredComponent {
        EndBarToolWindow(
            isToggled = isToggled,
        )
    }

    override val endBar by endBar {
        EndBarActionButton(
            icon = painterResource(R.drawable.ic_endbar),
            contentDescription = stringResource(R.string.endbar_action_cd),
            onClick = {
                // Action: no-op in sample; host SnackBar would confirm tap
            },
        )
        EndBarToggleButton(
            icon = painterResource(R.drawable.ic_endbar),
            contentDescription = stringResource(R.string.endbar_toggle_cd),
            isChecked = isToggled,
            onCheckedChange = { isToggled = it },
        )
    }
}
