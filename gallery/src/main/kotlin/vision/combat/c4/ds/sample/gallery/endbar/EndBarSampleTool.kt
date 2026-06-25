package vision.combat.c4.ds.sample.gallery.endbar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.endbar.ui.EndBarSampleWindow
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

/**
 * Demonstrates the full EndBar API.
 *
 * SDK APIs: AbstractTool.endBar, EndBarActionButton, EndBarToggleButton,
 *           EndBarMenuButton, EndBarMenuScope.Checkable, EndBarMenuScope.Slider,
 *           plugin painterResource on EndBar icons (FallbackResources).
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolEndBar.kt
 *   c4ds-sdk-core/ui/src/main/kotlin/vision/combat/c4/ds/sdk/ui/component/bar/endbar/EndBar.kt
 */
class EndBarSampleToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.endbar_tool_name
    override val iconResId: Int = R.drawable.ic_end_bar
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return EndBarSampleTool(toolContext, this, di, params)
    }
}

internal class EndBarSampleTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    private var toggleState by mutableStateOf(false)
    private var sliderValue by mutableFloatStateOf(0.5f)

    override val window: ToolComponent.Window by requiredComponent {
        EndBarSampleWindow(
            toggleState = { toggleState },
            sliderValue = { sliderValue },
        )
    }

    override val endBar by endBar {
        EndBarActionButton(
            icon = painterResource(R.drawable.ic_end_bar),
            contentDescription = stringResource(R.string.endbar_action_cd),
            onClick = { /* action no-op in sample */ },
        )
        EndBarToggleButton(
            icon = painterResource(R.drawable.ic_end_bar),
            contentDescription = stringResource(R.string.endbar_toggle_cd),
            isChecked = toggleState,
            onCheckedChange = { toggleState = it },
        )
        EndBarMenuButton(
            icon = painterResource(R.drawable.ic_end_bar),
            contentDescription = stringResource(R.string.endbar_menu_cd),
        ) {
            Checkable(
                label = stringResource(R.string.endbar_menu_option_a),
                isChecked = toggleState,
                onClick = { toggleState = !toggleState },
            )
            Slider(
                label = stringResource(R.string.endbar_slider_label),
                value = sliderValue,
                onValueChange = { sliderValue = it },
                valueRange = 0f..1f,
            )
        }
    }
}

