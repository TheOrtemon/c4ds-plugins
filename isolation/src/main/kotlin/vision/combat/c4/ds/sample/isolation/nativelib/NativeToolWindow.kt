package vision.combat.c4.ds.sample.isolation.nativelib

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow
import vision.combat.c4.ds.sample.isolation.R
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar

@Composable
internal fun NativeToolWindow(
    jniResult: StateFlow<String?>,
    assetResult: StateFlow<String?>,
) {
    val jni by jniResult.collectAsState()
    val asset by assetResult.collectAsState()

    WindowScaffold(
        topAppBar = {
            BackNavTopAppBar(title = stringResource(R.string.native_tool_name))
        },
        content = { NativeContent(jni, asset) },
    )
}

@Composable
private fun ColumnScope.NativeContent(jniResult: String?, assetResult: String?) {
    Text(
        text = stringResource(R.string.native_cross_apk_explainer),
        style = MaterialTheme.typography.body2,
        modifier = Modifier.padding(bottom = 16.dp),
    )

    ResultCard(
        label = stringResource(R.string.native_jni_label),
        value = jniResult ?: stringResource(R.string.native_not_loaded),
    )

    ResultCard(
        label = stringResource(R.string.native_asset_label),
        value = assetResult ?: stringResource(R.string.native_not_loaded),
    )
}

@Composable
private fun ResultCard(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(bottom = 4.dp),
        )
        Card(
            elevation = 2.dp,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(12.dp),
            )
        }
    }
}

