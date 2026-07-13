package vision.combat.c4.ds.sample.gallery.model.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.domain.interactor.CommonMapInteractor
import vision.combat.c4.ds.sdk.domain.interactor.CommonModelInteractor
import vision.combat.c4.ds.sdk.domain.model.Hostility
import vision.combat.c4.ds.sdk.domain.model.ModelAttrs
import vision.combat.c4.ds.sdk.domain.util.toGeoPoint
import vision.combat.c4.ds.sdk.ui.component.button.OutlinedButton
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel

/** A single example: a MIL-STD-2525 symbol key paired with an affiliation. */
private data class SymbolExample(
    val symbolKey: Int,
    val hostility: Hostility,
    val labelResId: Int,
)

private val SYMBOL_EXAMPLES = listOf(
    SymbolExample(1160, Hostility.Friend, R.string.model_sc_symbol_rifleman),
    SymbolExample(153, Hostility.Hostile, R.string.model_sc_symbol_air),
    SymbolExample(2, Hostility.Neutral, R.string.model_sc_symbol_civil),
    SymbolExample(4, Hostility.Unknown, R.string.model_sc_symbol_fire),
    SymbolExample(14, Hostility.Suspect, R.string.model_sc_symbol_hazmat),
)

/**
 * Shows how the same interactor builds models with DIFFERENT MIL-STD-2525 symbol keys and
 * affiliations: each row previews the rendered symbol and creates+consumes that model on tap.
 *
 * The symbol key (an Int handle into the binding table) is passed via [ModelAttrs.symbolKey]; the
 * affiliation (color/frame) is the orthogonal [ModelAttrs.hostility].
 */
@Composable
internal fun ColumnScope.SymbolKeysShowcase(viewModel: SymbolKeysViewModel = diViewModel()) {
    Text(
        text = stringResource(R.string.model_sc_symbols_explainer),
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.onSurface,
    )
    Divider()
    SYMBOL_EXAMPLES.forEach { example ->
        val label = stringResource(example.labelResId)
        SymbolExampleRow(
            example = example,
            label = label,
            onCreate = { viewModel.create(example.symbolKey, example.hostility, label) },
        )
    }
}

@Composable
private fun SymbolExampleRow(
    example: SymbolExample,
    label: String,
    onCreate: () -> Unit,
) {
    val painter = rememberSymbolPainterOrNull(example.symbolKey, example.hostility)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
            Icon(
                painter = painter ?: rememberVectorPainter(Icons.Default.Place),
                contentDescription = null,
                tint = if (painter != null) Color.Unspecified else MaterialTheme.colors.onSurface,
                modifier = Modifier.matchParentSize(),
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = MaterialTheme.typography.body1, color = MaterialTheme.colors.onSurface)
            Text(
                text = "key ${example.symbolKey} · ${example.hostility.name}",
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
            )
        }
        OutlinedButton(label = stringResource(R.string.model_sc_create_short), onClick = onCreate)
    }
}

internal class SymbolKeysViewModel(
    private val modelInteractor: CommonModelInteractor,
    private val mapInteractor: CommonMapInteractor,
) : ViewModel() {

    /** Create + consume a model with the given symbol key and affiliation at the selected position. */
    fun create(symbolKey: Int, hostility: Hostility, name: String) {
        val startPoint = mapInteractor.selectedPosition.value.toGeoPoint(withAltitude = true)
        val attrs = ModelAttrs(symbolKey = symbolKey, hostility = hostility.name, name = name)
        val model = modelInteractor.createModel(startPoint, attrs, { point, _, _ -> point }, templated = true)
            ?: return
        modelInteractor.consumeModel(model, attachToDefault = true)
        mapInteractor.requestRedraw()
    }
}
