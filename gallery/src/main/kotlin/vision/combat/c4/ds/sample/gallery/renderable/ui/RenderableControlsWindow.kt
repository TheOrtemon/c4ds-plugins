package vision.combat.c4.ds.sample.gallery.renderable.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.StateFlow
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar

/**
 * Full-panel window that wraps [RenderableControls] with a [BackNavTopAppBar] header.
 *
 * Displayed via [vision.combat.c4.ds.sdk.tool.ToolComponent.Window] / requiredComponent so the user
 * can open it from the tool panel rather than seeing the controls only in the status bar.
 */
@Composable
internal fun RenderableControlsWindow(
    addedCount: StateFlow<Int>,
    selectedColor: StateFlow<RenderableColor>,
    onColorSelected: (RenderableColor) -> Unit,
    onAddPoint: () -> Unit,
    onAddLine: () -> Unit,
    onAddPolygon: () -> Unit,
    onAddCircle: () -> Unit,
    onAddLabel: () -> Unit,
    onClear: () -> Unit,
) {
    WindowScaffold(
        topAppBar = {
            BackNavTopAppBar(title = stringResource(R.string.renderable_tool_name))
        },
        content = {
            RenderableControls(
                addedCount = addedCount,
                selectedColor = selectedColor,
                onColorSelected = onColorSelected,
                onAddPoint = onAddPoint,
                onAddLine = onAddLine,
                onAddPolygon = onAddPolygon,
                onAddCircle = onAddCircle,
                onAddLabel = onAddLabel,
                onClear = onClear,
            )
        },
    )
}
