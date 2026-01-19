package vision.combat.c4.ds.example.tool.aliasing

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAddCheck
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.checkable.SwitchField
import vision.combat.c4.ds.sdk.ui.component.text.OutlinedTextInputField
import vision.combat.c4.ds.sdk.ui.util.showToast
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel
import vision.combat.c4.ds.tool.sample.aliasing.R

@Composable
internal fun AliasingToolWindow(viewModel: AliasingToolViewModel = diViewModel()) {
    WindowContent(
        uiState = viewModel.uiState,
        viewModel = viewModel
    )
    EffectHandler(viewModel.effects)

    val context = LocalContext.current
    LaunchedEffect(viewModel, context) {
        val assets = context
                .resources
                .getStringArray(R.array.embedded_alias_list_ukrainian)
                .toList()
        viewModel.initAssets(assets)
    }
}

@Composable
private fun WindowContent(uiState: AliasingToolViewModel.UiState, viewModel: AliasingToolViewModel) {
    WindowScaffold(
        topAppBar = { AliasingTopAppBar(viewModel) },
    ) {
        SwitchField(
            initialValue = uiState.isRandomized,
            stringResource(R.string.the_is_randomized),
            onCheckedChange = { viewModel.updateIsRandomized(it) }
        )
        SwitchField(
            initialValue = uiState.isRandomColor,
            stringResource(R.string.the_is_model_colors_randomized),
            onCheckedChange = { viewModel.updateIsRandomColor(it) }
        )
        if (!uiState.isRandomized) {
            OutlinedTextInputField(
                value = uiState.aliasList,
                label = stringResource(R.string.user_input_alias_list),
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 260.dp),
                onValueChange = { viewModel.updateAliasList(it) },
                singleLine = false,
            )
        }
    }
}

@Composable
private fun AliasingTopAppBar(viewModel: AliasingToolViewModel) {
    BackNavTopAppBar(
        title = stringResource(R.string.the_aliasing_tool_name),
        actions = {
            IconButton(
                onClick = {
                    viewModel.massProcess()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.PlaylistAddCheck,
                    contentDescription = stringResource(R.string.select_the_model),
                )
            }
        }
    )
}

@Composable
private fun EffectHandler(effectFlow: Flow<Effect>) {
    val context = LocalContext.current

    LaunchedEffect(effectFlow, context) {
        effectFlow.collect { effect ->
            val message = when (effect) {
                Effect.ModelNotSelected -> R.string.no_model_selected
                Effect.NoLayers -> R.string.no_layers
                Effect.NoAliases -> R.string.no_aliases
                Effect.NotEnoughAliases -> R.string.not_enough_aliases
                Effect.Done -> R.string.done
            }
            context.showToast(message)
        }
    }
}

