package vision.combat.c4.ds.sample.openwith

import android.content.Intent
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.button.Button
import vision.combat.c4.ds.sdk.ui.platform.LocalShareManager
import java.io.File

@Composable
internal fun OpenWithToolWindow(toolContext: ToolContext) {
    WindowScaffold(
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.open_with_tool_name)) },
        content = { OpenWithContent(toolContext) },
    )
}

@Composable
private fun ColumnScope.OpenWithContent(toolContext: ToolContext) {
    val context = LocalContext.current
    val shareManager = LocalShareManager.current
    val sampleFileContent = stringResource(R.string.open_with_sample_file_content)
    val outsideRootsMessage = stringResource(R.string.open_with_outside_roots)
    val chooserTitle = stringResource(R.string.open_with_chooser_title)
    var message by remember { mutableStateOf<String?>(null) }

    Text(
        text = stringResource(R.string.open_with_explainer),
        style = MaterialTheme.typography.body2,
        modifier = Modifier.padding(bottom = 16.dp),
    )

    Button(
        label = stringResource(R.string.open_with_button),
        onClick = {
            // "export/" is the sub-directory of the HOST's cache dir that its own FileProvider
            // <cache-path> declares. getShareableUri resolves its argument through that
            // (host-owned) FileProvider; a file written directly to the cache dir *root* isn't
            // covered by any declared path, so getShareableUri returns null for it. Writing under
            // "export/" keeps this demo on the supported, non-null path — see
            // gallery/.../hostservices/ui/HostServicesWindow.kt for the same convention.
            val exportDir = File(toolContext.cacheDir, "export")
            exportDir.mkdirs()
            val file = File(exportDir, SAMPLE_FILE_NAME)
            file.writeText(sampleFileContent)

            val uri = shareManager.getShareableUri(file)
            if (uri == null) {
                message = outsideRootsMessage
                return@Button
            }

            val viewIntent = Intent(Intent.ACTION_VIEW)
                .setDataAndType(uri, "text/plain")
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(
                Intent.createChooser(viewIntent, chooserTitle)
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
            )
            message = null
        },
    )

    message?.let {
        Text(
            text = it,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(top = 8.dp),
        )
    }

    Text(
        text = stringResource(R.string.open_with_rule_caption),
        style = MaterialTheme.typography.caption,
        modifier = Modifier.padding(top = 16.dp),
    )
}

private const val SAMPLE_FILE_NAME = "open_with_sample.txt"
