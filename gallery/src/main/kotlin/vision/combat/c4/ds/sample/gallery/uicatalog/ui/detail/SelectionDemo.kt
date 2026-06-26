package vision.combat.c4.ds.sample.gallery.uicatalog.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.ui.component.ColorSelector
import vision.combat.c4.ds.sdk.ui.component.RadioGroup
import vision.combat.c4.ds.sdk.ui.component.RadioOption
import vision.combat.c4.ds.sdk.ui.component.SegmentedButtonItem
import vision.combat.c4.ds.sdk.ui.component.SegmentedButtonRow
import vision.combat.c4.ds.sdk.ui.component.checkable.CheckBoxField
import vision.combat.c4.ds.sdk.ui.component.checkable.SwitchField
import vision.combat.c4.ds.sdk.ui.component.slider.SliderWithLabel

private enum class SegmentChoice { A, B, C }

@Composable
internal fun SelectionDemo() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        DemoSection(stringResource(R.string.components_segmented)) {
            var selected by remember { mutableStateOf(SegmentChoice.A) }
            val items = remember {
                listOf(
                    SegmentedButtonItem(SegmentChoice.A, "A"),
                    SegmentedButtonItem(SegmentChoice.B, "B"),
                    SegmentedButtonItem(SegmentChoice.C, "C"),
                )
            }
            SegmentedButtonRow(
                items = items,
                selected = selected,
                onSelected = { selected = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        DemoSection(stringResource(R.string.components_slider)) {
            var value by remember { mutableFloatStateOf(0.5f) }
            SliderWithLabel(
                value = value,
                valueRange = 0f..1f,
                onValueChange = { value = it },
                onValueChangeFinished = {},
                label = { Text(text = "%.2f".format(value), color = MaterialTheme.colors.onSurface) },
            )
        }
        DemoSection(stringResource(R.string.components_checkable)) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                CheckBoxField(
                    initialValue = true,
                    label = stringResource(R.string.components_checkbox_label),
                    onCheckedChange = {},
                )
                SwitchField(
                    initialValue = false,
                    label = stringResource(R.string.components_switch_label),
                    onCheckedChange = {},
                )
            }
        }
        DemoSection(stringResource(R.string.components_radio)) {
            var selected by remember { mutableIntStateOf(0) }
            val options = listOf(
                RadioOption(label = stringResource(R.string.components_radio_option_1)),
                RadioOption(label = stringResource(R.string.components_radio_option_2)),
                RadioOption(label = stringResource(R.string.components_radio_option_3)),
            )
            RadioGroup(
                options = options,
                selection = selected,
                onSelectionChange = { selected = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        DemoSection(stringResource(R.string.components_color_selector)) {
            var color by remember { mutableStateOf(Color.Red) }
            ColorSelector(
                selectedColor = color,
                onSelectColorClick = { color = it },
                readOnly = false,
                label = stringResource(R.string.components_color_selector_label),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
