package vision.combat.c4.ds.sample.gallery.model.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sdk.domain.model.Hostility
import vision.combat.c4.ds.sdk.ui.component.IconWithText
import vision.combat.c4.ds.sdk.ui.component.list.ListItem
import vision.combat.c4.ds.sdk.ui.component.list.ListItemDefaults
import vision.combat.c4.model.BattlespaceConceptModel

/** A bold section label used across the model showcases. */
@Composable
internal fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.subtitle2,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(top = 4.dp),
    )
}

/** A label + value readout row used by the selection/events showcase. */
@Composable
internal fun Readout(label: String, value: String) {
    Text(
        text = "$label: $value",
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.onSurface,
    )
}

/**
 * A TacticalData-style model row: MIL-STD-2525 symbol leading icon + name + coordinates + affiliation.
 * Reused by the models-list showcase (and others) to mirror the host TacticalData list visuals.
 */
@Composable
internal fun ModelRow(
    model: BattlespaceConceptModel,
    onClick: (() -> Unit)? = null,
    canGoForward: Boolean = false,
) {
    val hostility = Hostility.findByName(model.hostilityCode.name)
    val symbolPainter = rememberSymbolPainterOrNull(model.symbolKey, hostility)

    val center = model.location.center
    val coordText = "%.4f°, %.4f°".format(center.lat, center.lon)

    ListItem(
        headline = {
            Text(
                text = model.name ?: model.id.toString(),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        supportingText = {
            IconWithText(
                painter = rememberVectorPainter(Icons.Default.LocationOn),
                text = coordText,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
            )
        },
        leadingIcon = {
            Box(modifier = Modifier.size(ListItemDefaults.LeadingIconSize)) {
                if (symbolPainter != null) {
                    Icon(
                        painter = symbolPainter,
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.matchParentSize(),
                    )
                } else {
                    Icon(
                        painter = rememberVectorPainter(Icons.Default.Place),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface,
                        modifier = Modifier.matchParentSize(),
                    )
                }
            }
        },
        contentTrailingTop = {
            IconWithText(
                painter = rememberVectorPainter(Icons.Default.MyLocation),
                text = model.hostilityCode.name,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
            )
        },
        onItemClick = onClick,
        canGoForward = canGoForward,
    )
}

/** Resolves the MIL-STD-2525 symbol painter at the list leading-icon size; null if unavailable. */
@Composable
internal fun rememberSymbolPainterOrNull(symbolKey: Int, hostility: Hostility) =
    vision.combat.c4.ds.sdk.ui.util.renderer.rememberSymbolPainter(
        symbolKey = symbolKey,
        hostility = hostility,
        size = ListItemDefaults.LeadingIconSize,
    )
