package vision.combat.c4.ds.example.tool.polka_dots

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAddCheck
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.PublishedWithChanges
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import vision.combat.c4.ds.sdk.ui.component.ColorSelector
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.checkable.SwitchField
import vision.combat.c4.ds.sdk.ui.component.text.OutlinedTextInputField
import vision.combat.c4.ds.sdk.ui.util.showToast
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel
import vision.combat.c4.ds.tool.sample.polka_dots.R

@Composable
internal fun PolkaDotsToolWindow(viewModel: PolkaDotsToolViewModel = diViewModel()) {
    WindowContent(
        uiState = viewModel.uiState,
        viewModel = viewModel
    )

    EffectHandler(viewModel.effects)
}

@Composable
private fun WindowContent(uiState: PolkaDotsToolViewModel.UiState, viewModel: PolkaDotsToolViewModel) {
    WindowScaffold(
        topAppBar = { PolkaTopAppBar(viewModel) },
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
            OutlinedTextInputField(
                modifier = Modifier.weight(1f),
                value = uiState.distance,
                onValueChange = { viewModel.updateDistance(it) },
                label = stringResource(R.string.step_in_meters),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Spacer(modifier = Modifier.width(4.dp))
            if (!uiState.isPrefixFromModel) {
                OutlinedTextInputField(
                    modifier = Modifier.weight(1f),
                    value = uiState.prefix,
                    onValueChange = { viewModel.updatePrefix(it) },
                    label = stringResource(R.string.prefix),
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            OutlinedTextInputField(
                modifier = Modifier.weight(1f),
                value = uiState.startingNumber,
                onValueChange = { viewModel.updateStartingNumber(it) },
                label = stringResource(R.string.starting_number),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        if (!uiState.isColorFromModel) {
            Row (
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.choose_points_color),
                    modifier = Modifier.weight(1f)
                )
                ColorSelector(
                    uiState.color,
                    onSelectColorClick = { viewModel.updateColor(it) },
                    modifier = Modifier.padding(end = 10.dp),
                    readOnly = false
                )
            }
        }
        SwitchField(
            initialValue = uiState.isColorFromModel,
            stringResource(R.string.is_color_from_model),
            onCheckedChange = { viewModel.updateIsColorFromModel(it) }
        )
        SwitchField(
            initialValue = uiState.isPrefixFromModel,
            stringResource(R.string.is_prefix_from_model),
            onCheckedChange = { viewModel.updateIsPrefixFromModel(it) }
        )
        SwitchField(
            initialValue = uiState.isModelDeleted,
            stringResource(R.string.is_model_deleted),
            onCheckedChange = { viewModel.updateIsModelRetained(it) }
        )
        SwitchField(
            initialValue = uiState.isReversedOrder,
            stringResource(R.string.is_reversed_order),
            onCheckedChange = { viewModel.updateIsReversedOrder(it) }
        )
    }
}

@Composable
private fun PolkaTopAppBar(viewModel: PolkaDotsToolViewModel) {
    BackNavTopAppBar(
        title = stringResource(R.string.polka_dots_tool_name),
        actions = {
            IconButton(
                onClick = { viewModel.process() },
            ) {
                Icon(
                    imageVector = Icons.Filled.PublishedWithChanges,
                    contentDescription = stringResource(R.string.select_the_model),
                )
            }
            IconButton(
                onClick = { viewModel.massProcess() },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.PlaylistAddCheck,
                    contentDescription = stringResource(R.string.select_the_model),
                )
            }
        },
    )
}

@Composable
private fun EffectHandler(effectFlow: Flow<PolkaEffect>) {
    val context = LocalContext.current

    LaunchedEffect(effectFlow, context) {
        effectFlow.collect { effect ->
            val message = when (effect) {
                PolkaEffect.ModelNotSelected -> R.string.no_model_selected
                PolkaEffect.NoLayers -> R.string.no_layers
                PolkaEffect.Done -> R.string.done
            }
            context.showToast(message)
        }
    }
}