package vision.combat.c4.ds.sample.gallery.components

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Catalog-launched sample showcasing SDK UI components on one scrollable screen.
 *
 * SDK APIs demonstrated:
 *   button.Button, button.OutlinedButton, button.TextButton, button.DestructiveButton,
 *   button.PrimaryProgressButton, button.AppFab,
 *   SegmentedButtonRow, SegmentedButtonItem,
 *   text.OutlinedTextInputField,
 *   IntegerStepper,
 *   dropdown.SimpleDropDownField, dropdown.OutlinedDropDownField,
 *   slider.SliderWithLabel,
 *   checkable.CheckBoxField, checkable.SwitchField,
 *   RadioGroup, RadioOption,
 *   dialog.AppDialog, dialog.DialogHeader, dialog.ButtonsRow,
 *   measurement.DistanceInput, measurement.SpeedInput, measurement.AltitudeInput, measurement.AngleInput,
 *   coordinates.CoordinatesInputWithSystem,
 *   bar.TopAppBar, bar.AppBarActionButton,
 *   Banner, MessageType,
 *   Carousel,
 *   ColorSelector,
 *   Tooltip
 */
class ComponentsToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.components_tool_name
    override val iconResId: Int = R.drawable.ic_components
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return ComponentsTool(toolContext, this, di, params)
    }
}
