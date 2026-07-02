package vision.combat.c4.ds.sample.gallery.uicatalog.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.domain.model.Hostility
import vision.combat.c4.ds.sdk.ui.component.hostility.HostilitySelector
import vision.combat.c4.ds.sdk.ui.util.renderer.rememberSymbolPainter

/** A generic unit symbol key (rifleman) used to demonstrate affiliation rendering. */
private const val DEMO_SYMBOL_KEY = 1160

private val ICON_SIZE = 24.dp

@Composable
internal fun HostilitySelectorDemo() {
    var selected by remember { mutableIntStateOf(0) }
    val hostilities = remember {
        listOf(
            Hostility.Unknown,
            Hostility.Friend,
            Hostility.Hostile,
            Hostility.Neutral,
            Hostility.Suspect,
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        StateLabel(stringResource(R.string.ui_catalog_state_enabled))
        HostilitySelector(
            label = stringResource(R.string.ui_catalog_hostility_label),
            itemCount = hostilities.size,
            horizontalPadding = 0.dp,
        ) { index ->
            HostilitySwatch(
                hostility = hostilities[index],
                selected = selected == index,
                enabled = true,
                onClick = { selected = index },
            )
        }

        StateLabel(stringResource(R.string.ui_catalog_state_disabled))
        HostilitySelector(
            label = stringResource(R.string.ui_catalog_hostility_readonly_label),
            itemCount = hostilities.size,
            labelEnabled = false,
            isError = true,
            errorMessage = stringResource(R.string.ui_catalog_hostility_error),
            horizontalPadding = 0.dp,
        ) { index ->
            HostilitySwatch(
                hostility = hostilities[index],
                selected = index == 1,
                enabled = false,
                onClick = {},
            )
        }
    }
}

@Composable
private fun HostilitySwatch(
    hostility: Hostility,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val shape = MaterialTheme.shapes.small
    val painter = rememberSymbolPainter(DEMO_SYMBOL_KEY, hostility, ICON_SIZE)

    Box(
        modifier = Modifier
            .size(44.dp)
            .then(if (selected) Modifier.border(2.dp, MaterialTheme.colors.primary, shape) else Modifier)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (painter != null) {
            Image(
                painter = painter,
                contentDescription = "${stringResource(R.string.ui_catalog_hostility_label)}: ${hostility.name}",
                modifier = Modifier.size(ICON_SIZE),
                alpha = if (enabled) 1f else 0.4f,
            )
        } else {
            Icon(
                painter = rememberVectorPainter(Icons.Default.Place),
                contentDescription = hostility.name,
                tint = MaterialTheme.colors.onSurface.copy(alpha = if (enabled) 1f else 0.4f),
                modifier = Modifier.size(ICON_SIZE),
            )
        }
    }
}
