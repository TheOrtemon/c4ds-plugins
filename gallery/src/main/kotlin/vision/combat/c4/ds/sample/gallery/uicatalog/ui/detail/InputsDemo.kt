package vision.combat.c4.ds.sample.gallery.uicatalog.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.ui.component.IntegerStepper
import vision.combat.c4.ds.sdk.ui.component.coordinates.CoordinatesInputWithSystem
import vision.combat.c4.ds.sdk.ui.component.dropdown.OutlinedDropDownField
import vision.combat.c4.ds.sdk.ui.component.dropdown.SimpleDropDownField
import vision.combat.c4.ds.sdk.ui.component.measurement.AltitudeInput
import vision.combat.c4.ds.sdk.ui.component.measurement.AngleInput
import vision.combat.c4.ds.sdk.ui.component.measurement.DistanceInput
import vision.combat.c4.ds.sdk.ui.component.measurement.SpeedInput
import vision.combat.c4.ds.sdk.ui.component.text.OutlinedTextInputField

@Composable
internal fun InputsDemo() {
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
