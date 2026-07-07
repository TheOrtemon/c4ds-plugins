package vision.combat.c4.ds.sample.gallery.uicatalog.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.domain.model.Hostility
import vision.combat.c4.ds.sdk.ui.component.hostility.HostilitySelector

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
            value = hostilities[selected],
            onValueChange = { selected = hostilities.indexOf(it) },
            options = hostilities,
            label = stringResource(R.string.ui_catalog_hostility_label),
            horizontalPadding = 0.dp,
        )

        StateLabel(stringResource(R.string.ui_catalog_state_disabled))
        HostilitySelector(
            value = hostilities[1],
            onValueChange = {},
            options = hostilities,
            label = stringResource(R.string.ui_catalog_hostility_readonly_label),
            enabled = false,
            isError = true,
            errorMessage = stringResource(R.string.ui_catalog_hostility_error),
            horizontalPadding = 0.dp,
        )
    }
}
