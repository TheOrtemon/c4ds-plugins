package vision.combat.c4.ds.sample.sight

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import vision.combat.c4.ds.example.tool.sight.SightToolViewModel.UiState
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.checkable.SwitchField
import vision.combat.c4.ds.sdk.ui.component.dropdown.SimpleDropDownField
import vision.combat.c4.ds.sdk.ui.theme.warning
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel
import vision.combat.c4.ds.sample.sight.R


@Composable
internal fun Window(viewModel: SightToolViewModel = diViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.updateChannelList()
    }
    WindowContent(
        uiState = uiState,
        onPilotChange = viewModel::updatePilotMode,
        onSelectedChannel = viewModel::updateSelectedChannelName,
    )
}

@Composable
private fun WindowContent(
    uiState: UiState,
    onPilotChange: (Boolean) -> Unit,
    onSelectedChannel: (String) -> Unit
) {
    WindowScaffold( topAppBar = { TopAppBar() } ) {
        SwitchField(
            initialValue = uiState.isPilot,
            stringResource(R.string.is_pilot),
            onCheckedChange = onPilotChange
        )
        SimpleDropDownField(
            options = uiState.channelNames,
            selectedIndex = uiState.channelNames.indexOf(uiState.selectedChannelName).takeIf { it >= 0 } ?: 0,
            onOptionSelected = { onSelectedChannel(uiState.channelNames[it]) },
        )
    }
}

@Composable
private fun TopAppBar() {
    BackNavTopAppBar(
        title = stringResource(R.string.sight_tool_name),
    )
}

@Composable
internal fun Overlay(viewModel: SightToolViewModel = diViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SightContent(
        uiState,
        onMapClick = { x, y ->
            viewModel.updateLocation(x, y)
            viewModel.sendCoordinate(x, y)
        }
    )
}

@Composable
private fun SightContent(
    uiState: UiState,
    onMapClick: (Float, Float) -> Unit
) {
    val density = LocalDensity.current
    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    val backgroundColor = if (uiState.isPilot) {
        Color.Black.copy(alpha = 0.0f)
    } else {
        Color.Black.copy(alpha = 1.0f)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .onGloballyPositioned { containerSize = it.size }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    if (containerSize.width > 0 && containerSize.height > 0) {
                        val percentX = offset.x / containerSize.width
                        val percentY = offset.y / containerSize.height
                        onMapClick(percentX, percentY)
                    }
                }
            }
    ) {
        if (uiState.xCoordinate != null && uiState.yCoordinate != null) {
            val xPx = uiState.xCoordinate * containerSize.width.toFloat()
            val yPx = uiState.yCoordinate * containerSize.height.toFloat()

            val xDp = with(density) { xPx.toDp() }
            val yDp = with(density) { yPx.toDp() }

            if (uiState.isVisible) {
                Box(
                    modifier = Modifier
                    .size(32.dp)
                    .offset(x = xDp, y = yDp)
                    .graphicsLayer {
                        translationX = -16f * density.density
                        translationY = -16f * density.density
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowUp,
                        contentDescription = null,
                        tint = MaterialTheme.colors.warning,
                        modifier = Modifier
                            .size(32.dp)
                            .offset(x = 0.dp, y = 2.dp)
                    )
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowUp,
                        contentDescription = "Cursor",
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .size(32.dp)
                    )
                }
            }
        }
    }
}
