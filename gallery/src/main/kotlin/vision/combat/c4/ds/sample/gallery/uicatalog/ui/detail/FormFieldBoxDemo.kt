package vision.combat.c4.ds.sample.gallery.uicatalog.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.ui.component.field.FormFieldBox
import vision.combat.c4.ds.sdk.ui.component.text.OutlinedTextInputField

@Composable
internal fun FormFieldBoxDemo() {
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
