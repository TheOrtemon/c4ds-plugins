package vision.combat.c4.ds.sample.gallery.network

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Network requests with the Ktor client the host already provides.
 *
 * The tool binds its **own** untagged [io.ktor.client.HttpClient] in its Kodein module — the
 * SDK deliberately keeps the untagged `HttpClient` slot free for plugins (the host's shared
 * client is bound under `SdkRemoteTags.HTTP_CLIENT`). Every Ktor and kotlinx-serialization
 * class arrives through `compileOnly(c4ds-sdk)` and is provided by the host at runtime —
 * nothing network-related is bundled into the plugin APK.
 *
 * The sample fetches current weather for the map's selected position from the keyless
 * Open-Meteo API and maps the response DTO to a domain model behind a repository interface.
 *
 * SDK APIs demonstrated:
 *   - HttpClient(Android) + ContentNegotiation + kotlinx-serialization json (host-provided Ktor)
 *   - CommonMapInteractor.selectedPosition
 *   - CommonLocaleSettingsInteractor.coordinateSystemFormat
 *
 * SDK files:
 *   c4ds-sdk-core/data/src/main/kotlin/vision/combat/c4/ds/sdk/data/di/RemoteModule.kt
 *   c4ds-sdk-core/data/src/main/kotlin/vision/combat/c4/ds/sdk/data/di/SdkRemoteTags.kt
 */
class NetworkToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.network_tool_name
    override val iconResId: Int = R.drawable.ic_network
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return NetworkTool(toolContext, this, di, params)
    }
}
