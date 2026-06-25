package vision.combat.c4.ds.sample.gallery.components.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import vision.combat.c4.ds.sdk.ui.component.Banner
import vision.combat.c4.ds.sdk.ui.component.Carousel
import vision.combat.c4.ds.sdk.ui.component.ColorSelector
import vision.combat.c4.ds.sdk.ui.component.IntegerStepper
import vision.combat.c4.ds.sdk.ui.component.MessageType
import vision.combat.c4.ds.sdk.ui.component.RadioGroup
import vision.combat.c4.ds.sdk.ui.component.RadioOption
import vision.combat.c4.ds.sdk.ui.component.SegmentedButtonItem
import vision.combat.c4.ds.sdk.ui.component.SegmentedButtonRow
import vision.combat.c4.ds.sdk.ui.component.TextAction
import vision.combat.c4.ds.sdk.ui.component.Tooltip
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.AppBarActionButton
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.bar.TopAppBar
import vision.combat.c4.ds.sdk.ui.component.button.AppFab
import vision.combat.c4.ds.sdk.ui.component.button.Button
import vision.combat.c4.ds.sdk.ui.component.button.DestructiveButton
import vision.combat.c4.ds.sdk.ui.component.button.OutlinedButton
import vision.combat.c4.ds.sdk.ui.component.button.PrimaryProgressButton
import vision.combat.c4.ds.sdk.ui.component.button.TextButton
import vision.combat.c4.ds.sdk.ui.component.checkable.CheckBoxField
import vision.combat.c4.ds.sdk.ui.component.checkable.SwitchField
import vision.combat.c4.ds.sdk.ui.component.coordinates.CoordinatesInputWithSystem
import vision.combat.c4.ds.sdk.ui.component.dialog.AppDialog
import vision.combat.c4.ds.sdk.ui.component.dialog.ButtonsRow
import vision.combat.c4.ds.sdk.ui.component.dialog.DialogHeader
import vision.combat.c4.ds.sdk.ui.component.dropdown.OutlinedDropDownField
import vision.combat.c4.ds.sdk.ui.component.dropdown.SimpleDropDownField
import vision.combat.c4.ds.sdk.ui.component.measurement.AltitudeInput
import vision.combat.c4.ds.sdk.ui.component.measurement.AngleInput
import vision.combat.c4.ds.sdk.ui.component.measurement.DistanceInput
import vision.combat.c4.ds.sdk.ui.component.measurement.SpeedInput
import vision.combat.c4.ds.sdk.ui.component.slider.SliderWithLabel
import vision.combat.c4.ds.sdk.ui.component.text.OutlinedTextInputField

@Composable
internal fun ComponentsWindow() {
    WindowScaffold(
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.components_tool_name)) },
        scrollable = false,
        contentPaddingValues = PaddingValues(0.dp),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item { Section(stringResource(R.string.components_buttons)) { ButtonsDemo() } }
            item { Section(stringResource(R.string.components_segmented)) { SegmentedDemo() } }
            item { Section(stringResource(R.string.components_text_input)) { TextInputDemo() } }
            item { Section(stringResource(R.string.components_stepper)) { StepperDemo() } }
            item { Section(stringResource(R.string.components_dropdowns)) { DropdownsDemo() } }
            item { Section(stringResource(R.string.components_slider)) { SliderDemo() } }
            item { Section(stringResource(R.string.components_checkable)) { CheckableDemo() } }
            item { Section(stringResource(R.string.components_radio)) { RadioDemo() } }
            item { Section(stringResource(R.string.components_dialog)) { DialogDemo() } }
            item { Section(stringResource(R.string.components_measurement)) { MeasurementDemo() } }
            item { Section(stringResource(R.string.components_coordinates)) { CoordinatesDemo() } }
            item { Section(stringResource(R.string.components_top_app_bar)) { TopAppBarDemo() } }
            item { Section(stringResource(R.string.components_banner)) { BannerDemo() } }
            item { Section(stringResource(R.string.components_carousel)) { CarouselDemo() } }
            item { Section(stringResource(R.string.components_color_selector)) { ColorSelectorDemo() } }
            item { Section(stringResource(R.string.components_tooltip)) { TooltipDemo() } }
            item { Section(stringResource(R.string.components_fab)) { FabDemo() } }
        }
    }
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onSurface,
        )
        content()
    }
}

@Composable
private fun ButtonsDemo() {
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

private enum class SegmentChoice { A, B, C }

@Composable
private fun SegmentedDemo() {
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

@Composable
private fun TextInputDemo() {
    var text by remember { mutableStateOf("") }
    OutlinedTextInputField(
        value = text,
        label = stringResource(R.string.components_text_input_label),
        onValueChange = { text = it },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun StepperDemo() {
    var count by remember { mutableIntStateOf(5) }
    IntegerStepper(
        value = count,
        label = stringResource(R.string.components_stepper_label),
        valueRange = 0..20,
        onValueChange = { count = it },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun DropdownsDemo() {
    val options = listOf(
        stringResource(R.string.components_dropdown_option_a),
        stringResource(R.string.components_dropdown_option_b),
        stringResource(R.string.components_dropdown_option_c),
        stringResource(R.string.components_dropdown_option_d),
    )
    var outlinedIndex by remember { mutableIntStateOf(0) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SimpleDropDownField(
            options = options,
            onOptionSelected = {},
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedDropDownField(
            options = options,
            selectedIndex = outlinedIndex,
            onOptionSelected = { outlinedIndex = it },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun SliderDemo() {
    var value by remember { mutableFloatStateOf(0.5f) }
    SliderWithLabel(
        value = value,
        valueRange = 0f..1f,
        onValueChange = { value = it },
        onValueChangeFinished = {},
        label = { Text(text = "%.2f".format(value), color = MaterialTheme.colors.onSurface) },
    )
}

@Composable
private fun CheckableDemo() {
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

@Composable
private fun RadioDemo() {
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

@Composable
private fun DialogDemo() {
    var showDialog by remember { mutableStateOf(false) }
    Button(
        label = stringResource(R.string.components_dialog_open),
        onClick = { showDialog = true },
    )
    if (showDialog) {
        AppDialog(
            onDismiss = { showDialog = false },
            header = {
                DialogHeader(
                    title = stringResource(R.string.components_dialog_title),
                    body = stringResource(R.string.components_dialog_body),
                )
            },
            buttons = {
                ButtonsRow {
                    TextButton(
                        label = stringResource(R.string.components_dialog_close),
                        onClick = { showDialog = false },
                    )
                }
            },
        )
    }
}

@Composable
private fun MeasurementDemo() {
    var distance by remember { mutableStateOf<Double?>(1500.0) }
    var speed by remember { mutableStateOf<Float?>(15f) }
    var altitude by remember { mutableStateOf<Double?>(120.0) }
    var azimuth by remember { mutableStateOf<Double?>(90.0) }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        DistanceInput(
            distanceMetres = distance,
            onValueChange = { distance = it },
            label = stringResource(R.string.components_measurement_distance),
            modifier = Modifier.fillMaxWidth(),
        )
        SpeedInput(
            speedMps = speed,
            onValueChange = { speed = it },
            label = stringResource(R.string.components_measurement_speed),
            modifier = Modifier.fillMaxWidth(),
        )
        AltitudeInput(
            altitudeMetres = altitude,
            onValueChange = { altitude = it },
            label = stringResource(R.string.components_measurement_altitude),
            modifier = Modifier.fillMaxWidth(),
        )
        AngleInput(
            angleDegrees = azimuth,
            onValueChange = { azimuth = it },
            label = stringResource(R.string.components_measurement_azimuth),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun CoordinatesDemo() {
    CoordinatesInputWithSystem(
        location = null,
        onLocationChanged = {},
        enabled = true,
    )
}

@Composable
private fun TopAppBarDemo() {
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
                    painter = androidx.compose.ui.graphics.vector.rememberVectorPainter(Icons.Default.Add),
                    label = stringResource(R.string.components_top_app_bar_action),
                    onClick = {},
                )
            },
        )
    }
}

@Composable
private fun BannerDemo() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Banner(type = MessageType.Info, message = stringResource(R.string.components_banner_info))
        Banner(type = MessageType.Warning, message = stringResource(R.string.components_banner_warning))
        Banner(
            type = MessageType.Error,
            message = stringResource(R.string.components_banner_error),
            action = TextAction(label = stringResource(R.string.components_banner_action)) {},
        )
    }
}

@Composable
private fun CarouselDemo() {
    val items = listOf(
        stringResource(R.string.components_carousel_item_1),
        stringResource(R.string.components_carousel_item_2),
        stringResource(R.string.components_carousel_item_3),
    )
    Carousel(data = items) { item ->
        Text(
            text = item,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }
}

@Composable
private fun ColorSelectorDemo() {
    var color by remember { mutableStateOf(Color.Red) }
    ColorSelector(
        selectedColor = color,
        onSelectColorClick = { color = it },
        readOnly = false,
        label = stringResource(R.string.components_color_selector_label),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun TooltipDemo() {
    val expanded = remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.components_tooltip_desc),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface,
        )
        Button(
            label = stringResource(R.string.components_tooltip_show),
            onClick = { expanded.value = !expanded.value },
        )
        Tooltip(
            expanded = expanded,
        ) {
            Text(
                text = stringResource(R.string.components_tooltip_content),
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}

@Composable
private fun FabDemo() {
    AppFab(
        imageVector = Icons.Default.Add,
        contentDescription = stringResource(R.string.components_fab_cd),
        onClick = {},
    )
}
