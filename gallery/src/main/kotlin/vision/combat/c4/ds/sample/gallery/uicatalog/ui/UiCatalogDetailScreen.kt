package vision.combat.c4.ds.sample.gallery.uicatalog.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.uicatalog.UiCatalogEntry
import vision.combat.c4.ds.sample.gallery.uicatalog.UiCatalogRegistry
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
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavigationButton
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
import vision.combat.c4.ds.sdk.ui.component.field.ExpandableField
import vision.combat.c4.ds.sdk.ui.component.field.FormFieldBox
import vision.combat.c4.ds.sdk.ui.component.field.HeaderField
import vision.combat.c4.ds.sdk.ui.component.field.InlineMessage
import vision.combat.c4.ds.sdk.ui.component.field.NestedForm
import vision.combat.c4.ds.sdk.ui.component.hostility.HostilitySelector
import vision.combat.c4.ds.sdk.ui.component.measurement.AltitudeInput
import vision.combat.c4.ds.sdk.ui.component.measurement.AngleInput
import vision.combat.c4.ds.sdk.ui.component.measurement.DistanceInput
import vision.combat.c4.ds.sdk.ui.component.measurement.SpeedInput
import vision.combat.c4.ds.sdk.ui.component.list.ListItem
import vision.combat.c4.ds.sdk.ui.component.reveal.DeleteMenuButton
import vision.combat.c4.ds.sdk.ui.component.reveal.EditMenuButton
import vision.combat.c4.ds.sdk.ui.component.reveal.RevealableLazyColumn
import vision.combat.c4.ds.sdk.ui.component.slider.SliderWithLabel
import vision.combat.c4.ds.sdk.ui.component.text.OutlinedTextInputField

@Composable
internal fun UiCatalogDetailScreen(
    componentId: String,
    onBack: () -> Unit,
) {
    val entry = UiCatalogRegistry.entryById(componentId)

    // Never call onBack() during composition — navigate after the frame via LaunchedEffect.
    if (entry == null) {
        LaunchedEffect(componentId) { onBack() }
        return
    }

    // The LISTS demo hosts a RevealableLazyColumn (a lazy list), which must not be nested
    // inside the scaffold's default verticalScroll. Give it its own non-scrolling scaffold.
    if (entry.id == UiCatalogRegistry.LISTS) {
        ListsDetailScreen(entry = entry, onBack = onBack)
        return
    }

    WindowScaffold(
        topAppBar = {
            TopAppBar(
                title = stringResource(entry.nameResId),
                navigationIcon = { BackNavigationButton(onBack) },
            )
        },
        content = { DetailContent(entry) },
    )
}

// ── Lists detail (RevealableLazyColumn — non-scrolling scaffold) ──────────────

private data class RevealItem(val id: Int)

@Composable
private fun ListsDetailScreen(entry: UiCatalogEntry, onBack: () -> Unit) {
    val items = remember { List(20) { i -> RevealItem(id = i) } }
    var refreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    WindowScaffold(
        scrollable = false,
        contentPaddingValues = PaddingValues(0.dp),
        topAppBar = {
            TopAppBar(
                title = stringResource(entry.nameResId),
                navigationIcon = { BackNavigationButton(onBack) },
            )
        },
    ) {
        RevealableLazyColumn(
            modifier = Modifier.fillMaxSize(),
            items = items,
            refreshing = refreshing,
            onRefresh = {
                refreshing = true
                scope.launch {
                    delay(1_500)
                    refreshing = false
                }
            },
            itemKey = { _, item -> item.id },
            startMenuItems = { _, _ ->
                EditMenuButton(onClick = {})
            },
            endMenuItems = { _, _ ->
                DeleteMenuButton(onClick = {})
            },
        ) { _, item ->
            ListItem(
                headline = {
                    Text(
                        text = stringResource(R.string.ui_catalog_list_item, item.id + 1),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface,
                    )
                },
                onItemClick = {},
            )
        }
    }
}

// ── Standard scrollable detail ────────────────────────────────────────────────

@Composable
private fun ColumnScope.DetailContent(entry: UiCatalogEntry) {
    Text(
        text = stringResource(entry.descResId),
        style = MaterialTheme.typography.body1,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 16.dp),
    )

    Divider(modifier = Modifier.padding(bottom = 16.dp))

    when (entry.id) {
        UiCatalogRegistry.INLINE_MESSAGE -> InlineMessageDemo()
        UiCatalogRegistry.HEADER_FIELD -> HeaderFieldDemo()
        UiCatalogRegistry.EXPANDABLE_FIELD -> ExpandableFieldDemo()
        UiCatalogRegistry.FORM_FIELD_BOX -> FormFieldBoxDemo()
        UiCatalogRegistry.NESTED_FORM -> NestedFormDemo()
        UiCatalogRegistry.HOSTILITY_SELECTOR -> HostilitySelectorDemo()
        UiCatalogRegistry.BUTTONS -> ButtonsDemo()
        UiCatalogRegistry.INPUTS -> InputsDemo()
        UiCatalogRegistry.SELECTION -> SelectionDemo()
        UiCatalogRegistry.FEEDBACK -> FeedbackDemo()
    }
}

@Composable
private fun StateLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.caption,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.onSurface,
    )
}

@Composable
private fun DemoSection(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        StateLabel(title)
        content()
    }
}

// ── InlineMessage ────────────────────────────────────────────────────────────

@Composable
private fun InlineMessageDemo() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        StateLabel(stringResource(R.string.ui_catalog_state_info))
        InlineMessage(InlineMessage.Info(stringResource(R.string.ui_catalog_inline_info_text)))

        StateLabel(stringResource(R.string.ui_catalog_state_error))
        InlineMessage(InlineMessage.Error(stringResource(R.string.ui_catalog_inline_error_text)))

        var visible by remember { mutableStateOf(true) }
        Button(
            label = stringResource(R.string.ui_catalog_toggle_message),
            onClick = { visible = !visible },
        )
        InlineMessage(
            if (visible) InlineMessage.Error(stringResource(R.string.ui_catalog_inline_error_text)) else null,
        )
    }
}

// ── HeaderField ──────────────────────────────────────────────────────────────

@Composable
private fun HeaderFieldDemo() {
    var clicks by remember { mutableIntStateOf(0) }
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        StateLabel(stringResource(R.string.ui_catalog_state_default))
        HeaderField(
            label = stringResource(R.string.ui_catalog_header_label),
            horizontalPadding = 0.dp,
            onClick = { clicks++ },
            trailingIcon = { Icon(Icons.AutoMirrored.Filled.ArrowRight, contentDescription = null) },
        )
        Text(
            text = stringResource(R.string.ui_catalog_header_clicks, clicks),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface,
        )

        StateLabel(stringResource(R.string.ui_catalog_header_label_custom_color))
        HeaderField(
            label = stringResource(R.string.ui_catalog_header_label_custom_color),
            horizontalPadding = 0.dp,
            backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.15f),
            onClick = {},
            trailingIcon = { Icon(Icons.AutoMirrored.Filled.ArrowRight, contentDescription = null) },
        )

        StateLabel(stringResource(R.string.ui_catalog_header_label_default_shape))
        HeaderField(
            label = stringResource(R.string.ui_catalog_header_label_default_shape),
            horizontalPadding = 0.dp,
            shape = MaterialTheme.shapes.large,
            onClick = {},
            trailingIcon = { Icon(Icons.AutoMirrored.Filled.ArrowRight, contentDescription = null) },
        )
    }
}

// ── ExpandableField ──────────────────────────────────────────────────────────

@Composable
private fun ExpandableFieldDemo() {
    var expanded by remember { mutableStateOf(false) }
    ExpandableField(
        label = stringResource(R.string.ui_catalog_expandable_label),
        isExpanded = expanded,
        onExpandChange = { expanded = !expanded },
        horizontalPadding = 0.dp,
    ) {
        Text(
            text = stringResource(R.string.ui_catalog_expandable_content),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(16.dp),
        )
    }
}

// ── FormFieldBox ─────────────────────────────────────────────────────────────

@Composable
private fun FormFieldBoxDemo() {
    val defaultInitial = stringResource(R.string.ui_catalog_field_value_default)
    val supportingInitial = stringResource(R.string.ui_catalog_field_value_supporting)
    val fieldLabel = stringResource(R.string.ui_catalog_field_label)

    var default by remember { mutableStateOf(defaultInitial) }
    var error by remember { mutableStateOf("") }
    var supporting by remember { mutableStateOf(supportingInitial) }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        StateLabel(stringResource(R.string.ui_catalog_state_default))
        FormFieldBox(horizontalPadding = 0.dp) {
            OutlinedTextInputField(
                value = default,
                label = fieldLabel,
                onValueChange = { default = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        StateLabel(stringResource(R.string.ui_catalog_state_error))
        FormFieldBox(
            horizontalPadding = 0.dp,
            errorMessage = stringResource(R.string.ui_catalog_form_field_box_error),
        ) {
            OutlinedTextInputField(
                value = error,
                label = fieldLabel,
                onValueChange = { error = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        StateLabel(stringResource(R.string.ui_catalog_state_supporting))
        FormFieldBox(
            horizontalPadding = 0.dp,
            supportingText = stringResource(R.string.ui_catalog_form_field_box_supporting),
        ) {
            OutlinedTextInputField(
                value = supporting,
                label = fieldLabel,
                onValueChange = { supporting = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

// ── NestedForm ───────────────────────────────────────────────────────────────

@Composable
private fun NestedFormDemo() {
    val parentInitial = stringResource(R.string.ui_catalog_nested_parent_value)
    val childInitial = stringResource(R.string.ui_catalog_nested_child_value)
    val deepInitial = stringResource(R.string.ui_catalog_nested_deep_value)

    var parent by remember { mutableStateOf(parentInitial) }
    var child by remember { mutableStateOf(childInitial) }
    var deep by remember { mutableStateOf(deepInitial) }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        StateLabel(stringResource(R.string.ui_catalog_nested_state_one_level))
        OutlinedTextInputField(
            value = parent,
            label = stringResource(R.string.ui_catalog_nested_parent_label),
            onValueChange = { parent = it },
            modifier = Modifier.fillMaxWidth(),
        )
        NestedForm {
            Column(
                modifier = Modifier.padding(start = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextInputField(
                    value = child,
                    label = stringResource(R.string.ui_catalog_nested_child_label),
                    onValueChange = { child = it },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        StateLabel(stringResource(R.string.ui_catalog_nested_state_two_levels))
        OutlinedTextInputField(
            value = parent,
            label = stringResource(R.string.ui_catalog_nested_parent_label),
            onValueChange = { parent = it },
            modifier = Modifier.fillMaxWidth(),
        )
        NestedForm {
            Column(
                modifier = Modifier.padding(start = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextInputField(
                    value = child,
                    label = stringResource(R.string.ui_catalog_nested_child_label),
                    onValueChange = { child = it },
                    modifier = Modifier.fillMaxWidth(),
                )
                NestedForm {
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        OutlinedTextInputField(
                            value = deep,
                            label = stringResource(R.string.ui_catalog_nested_deep_label),
                            onValueChange = { deep = it },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}

// ── HostilitySelector ────────────────────────────────────────────────────────

@Composable
private fun HostilitySelectorDemo() {
    var selected by remember { mutableIntStateOf(0) }
    val colors = remember {
        listOf(
            Color(0xFF9E9E9E),
            Color(0xFFD32F2F),
            Color(0xFFFBC02D),
            Color(0xFF388E3C),
            Color(0xFF1976D2),
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        StateLabel(stringResource(R.string.ui_catalog_state_enabled))
        HostilitySelector(
            label = stringResource(R.string.ui_catalog_hostility_label),
            itemCount = colors.size,
            horizontalPadding = 0.dp,
        ) { index ->
            HostilitySwatch(
                color = colors[index],
                selected = selected == index,
                enabled = true,
                onClick = { selected = index },
            )
        }

        StateLabel(stringResource(R.string.ui_catalog_state_disabled))
        HostilitySelector(
            label = stringResource(R.string.ui_catalog_hostility_readonly_label),
            itemCount = colors.size,
            labelEnabled = false,
            isError = true,
            errorMessage = stringResource(R.string.ui_catalog_hostility_error),
            horizontalPadding = 0.dp,
        ) { index ->
            HostilitySwatch(
                color = colors[index],
                selected = index == 1,
                enabled = false,
                onClick = {},
            )
        }
    }
}

@Composable
private fun HostilitySwatch(
    color: Color,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val shape = MaterialTheme.shapes.small
    Box(
        modifier = Modifier
            .size(44.dp)
            .then(if (selected) Modifier.border(2.dp, MaterialTheme.colors.primary, shape) else Modifier)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(color.copy(alpha = if (enabled) 1f else 0.4f), CircleShape),
        )
    }
}

// ── Buttons (folded from Components Showcase) ─────────────────────────────────

@Composable
private fun ButtonsDemo() {
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

// ── Inputs (folded from Components Showcase) ──────────────────────────────────

@Composable
private fun InputsDemo() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        DemoSection(stringResource(R.string.components_text_input)) {
            var text by remember { mutableStateOf("") }
            OutlinedTextInputField(
                value = text,
                label = stringResource(R.string.components_text_input_label),
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        DemoSection(stringResource(R.string.components_stepper)) {
            var count by remember { mutableIntStateOf(5) }
            IntegerStepper(
                value = count,
                label = stringResource(R.string.components_stepper_label),
                valueRange = 0..20,
                onValueChange = { count = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        DemoSection(stringResource(R.string.components_dropdowns)) {
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
        DemoSection(stringResource(R.string.components_measurement)) {
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
        DemoSection(stringResource(R.string.components_coordinates)) {
            CoordinatesInputWithSystem(
                location = null,
                onLocationChanged = {},
                enabled = true,
            )
        }
    }
}

// ── Selection (folded from Components Showcase) ───────────────────────────────

private enum class SegmentChoice { A, B, C }

@Composable
private fun SelectionDemo() {
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

// ── Feedback (folded from Components Showcase) ────────────────────────────────

@Composable
private fun FeedbackDemo() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        DemoSection(stringResource(R.string.components_dialog)) {
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
        DemoSection(stringResource(R.string.components_banner)) {
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
        DemoSection(stringResource(R.string.components_carousel)) {
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
        DemoSection(stringResource(R.string.components_tooltip)) {
            val expanded = remember { mutableStateOf(false) }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.components_tooltip_desc),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface,
                )
                Box {
                    Tooltip(expanded = expanded) {
                        Text(
                            text = stringResource(R.string.components_tooltip_content),
                            modifier = Modifier.padding(8.dp),
                        )
                    }
                    Button(
                        label = stringResource(R.string.components_tooltip_show),
                        onClick = { expanded.value = !expanded.value },
                    )
                }
            }
        }
    }
}
