package vision.combat.c4.ds.sample.gallery.uicatalog.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.ui.component.hostility.HostilitySelector

@Composable
internal fun HostilitySelectorDemo() {
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
