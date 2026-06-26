package vision.combat.c4.ds.sample.gallery.status.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import vision.combat.c4.ds.sample.gallery.R

@Composable
internal fun StatusBar() {
    Text(text = stringResource(R.string.status_tool_label))
}
