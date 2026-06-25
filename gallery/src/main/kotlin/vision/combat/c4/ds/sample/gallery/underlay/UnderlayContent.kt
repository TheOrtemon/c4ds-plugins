package vision.combat.c4.ds.sample.gallery.underlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.rememberInstance
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.ToolManager
import vision.combat.c4.ds.sdk.tool.deactivate
import vision.combat.c4.ds.sdk.ui.component.button.TextButton

@Composable
internal fun UnderlayContent() {
    val toolManager by rememberInstance<ToolManager>()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x33FF6600)),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(
            modifier = Modifier
                .background(Color(0x88000000))
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(R.string.underlay_label),
                style = MaterialTheme.typography.caption,
                color = Color.White,
            )
            TextButton(
                label = stringResource(R.string.underlay_close),
                onClick = { toolManager.deactivate<UnderlayToolDescriptor>() },
            )
        }
    }
}
