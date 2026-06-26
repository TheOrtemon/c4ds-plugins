package vision.combat.c4.ds.sample.gallery.uicatalog.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.ui.component.field.NestedForm
import vision.combat.c4.ds.sdk.ui.component.text.OutlinedTextInputField

@Composable
internal fun NestedFormDemo() {
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
