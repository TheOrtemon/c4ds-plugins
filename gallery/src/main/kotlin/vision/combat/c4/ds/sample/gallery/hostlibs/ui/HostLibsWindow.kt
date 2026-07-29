package vision.combat.c4.ds.sample.gallery.hostlibs.ui

import android.Manifest
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import armyc2.c5isr.renderer.utilities.SymbolUtilities
import coil3.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.component.button.Button

@Composable
internal fun HostLibsWindow() {
    WindowScaffold(
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.host_libs_tool_name)) },
        content = { HostLibsContent() },
    )
}

@Composable
private fun ColumnScope.HostLibsContent() {
    Text(
        text = stringResource(R.string.host_libs_desc),
        style = MaterialTheme.typography.body2,
        modifier = Modifier.padding(bottom = 16.dp),
    )

    CoilSection()
    Divider(modifier = Modifier.padding(vertical = 16.dp))
    PermissionsSection()
    Divider(modifier = Modifier.padding(vertical = 16.dp))
    SymbologySection()
}

@Composable
private fun ColumnScope.CoilSection() {
    SectionTitle(R.string.host_libs_coil_title)

    // Coil arrives via compileOnly(c4ds-sdk) — c4ds-sdk-core:ui declares it `api` precisely so
    // plugins can use it. The model is *this plugin's* drawable: AsyncImage resolves it through
    // LocalPlatformContext, which inside a tool component is the tool's own context, so the id is
    // looked up in this APK's resource table rather than the host's.
    AsyncImage(
        model = R.drawable.ic_host_libs,
        contentDescription = stringResource(R.string.host_libs_coil_image_desc),
        modifier = Modifier.size(48.dp),
    )
    Text(
        text = stringResource(R.string.host_libs_coil_caption),
        style = MaterialTheme.typography.caption,
        modifier = Modifier.padding(top = 8.dp),
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ColumnScope.PermissionsSection() {
    SectionTitle(R.string.host_libs_permissions_title)

    // The permission must be declared by the HOST manifest, not this plugin's: tool code runs in
    // the host process, so the request is made on the host's behalf. CAMERA is one the host
    // already declares.
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)
    val granted = cameraPermission.status is PermissionStatus.Granted

    Text(
        text = stringResource(
            if (granted) R.string.host_libs_permission_granted else R.string.host_libs_permission_denied
        ),
        style = MaterialTheme.typography.body2,
        modifier = Modifier.padding(bottom = 8.dp),
    )
    if (!granted) {
        Button(
            label = stringResource(R.string.host_libs_request_permission),
            onClick = { cameraPermission.launchPermissionRequest() },
        )
    }
}

@Composable
private fun ColumnScope.SymbologySection() {
    SectionTitle(R.string.host_libs_symbology_title)

    // mil-sym-android is `api` on :c4ds-sdk itself. A pure-function call is enough to prove the
    // class resolves — rendering a symbol would need the renderer's asset bootstrap, which is the
    // host's job, not a plugin's.
    val basicId = remember { SymbolUtilities.getBasicSymbolID2525C(SAMPLE_SYMBOL_ID) }

    Text(
        text = stringResource(R.string.host_libs_symbology_result, SAMPLE_SYMBOL_ID, basicId ?: "—"),
        style = MaterialTheme.typography.body2,
    )
}

@Composable
private fun ColumnScope.SectionTitle(resId: Int) {
    Text(
        text = stringResource(resId),
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier.padding(bottom = 8.dp),
    )
}

/** A standard MIL-STD-2525C friendly-ground-unit symbol id, used only as [SymbolUtilities] input. */
private const val SAMPLE_SYMBOL_ID = "SFGPUCI-----USG"
