package vision.combat.c4.ds.sample.gallery.endbar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.endbar.ui.EndBarWindow
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.endBar
import vision.combat.c4.ds.sdk.tool.requiredComponent
import vision.combat.c4.ds.sdk.ui.component.bar.endbar.EndBarActionButton
import vision.combat.c4.ds.sdk.ui.component.bar.endbar.EndBarMenuButton
import vision.combat.c4.ds.sdk.ui.component.bar.endbar.EndBarToggleButton
import vision.combat.c4.ds.sdk.ui.util.showToast

/**
 * Minimal AbstractTool wiring an EndBarWindow (ToolComponent.Window) and all three EndBar button
 * types: action, toggle, and menu with a Checkable item and a Slider.
 */
internal class EndBarTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    private var toggleState by mutableStateOf(false)
    private var sliderValue by mutableFloatStateOf(0.5f)

    override val window: ToolComponent.Window by requiredComponent {
        EndBarWindow(
            toggleState = { toggleState },
            sliderValue = { sliderValue },
        )
    }

    override val endBar by endBar {
        val context = LocalContext.current
        val actionFiredMessage = stringResource(R.string.endbar_action_fired)
        EndBarActionButton(
            icon = painterResource(R.drawable.ic_end_bar_action),
            contentDescription = stringResource(R.string.endbar_action_cd),
            onClick = { context.showToast(actionFiredMessage) },
        )
        EndBarToggleButton(
            icon = painterResource(R.drawable.ic_end_bar_toggle),
            contentDescription = stringResource(R.string.endbar_toggle_cd),
            isChecked = toggleState,
            onCheckedChange = { toggleState = it },
        )
        EndBarMenuButton(
            icon = painterResource(R.drawable.ic_end_bar_menu),
            title = stringResource(R.string.endbar_menu_cd),
            contentDescription = stringResource(R.string.endbar_menu_cd),
        ) {
            Checkable(
                title = stringResource(R.string.endbar_menu_option_a),
                isChecked = toggleState,
                onClick = { toggleState = !toggleState },
            )
            Slider(
                title = stringResource(R.string.endbar_slider_label),
                value = sliderValue,
                onValueChange = { sliderValue = it },
                range = 0f..1f,
            )
        }
    }
}
