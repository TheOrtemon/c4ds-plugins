package vision.combat.c4.ds.sample.gallery.service

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.bindSingletonOf
import org.kodein.di.instance
import org.kodein.di.subDI
import vision.combat.c4.ds.sample.gallery.service.di.serviceModule
import vision.combat.c4.ds.sdk.tool.AbstractToolService
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Session-scoped background service that simulates an inbox: every few seconds a "message" arrives,
 * bumping the unread counter in [ServiceSharedState].
 *
 * Created once at session start via [ToolDescriptor.createService] and kept alive for the whole
 * session — the loop keeps running even while the tool window is closed. [ServiceNotificationManager]
 * mirrors the unread count onto the tool-list badge, so the number appears on this tool's card while
 * it is inactive. The coroutine is cancelled automatically when [onDestroy] disposes [coroutineScope].
 */
internal class BadgeCounterService(
    toolContext: ToolContext,
    descriptor: ToolDescriptor,
    parentDI: DI,
) : AbstractToolService(toolContext, descriptor, parentDI) {

    override val di: DI = subDI(super.di) {
        import(serviceModule)
        bindSingletonOf(::ServiceNotificationManager)
    }

    private val sharedState: ServiceSharedState by instance()
    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.US)

    init {
        Log.i(TAG, "BadgeCounterService created — session service is running")
        coroutineScope.launch {
            while (isActive) {
                delay(MESSAGE_INTERVAL_MS)
                val timestamp = timeFormat.format(Date())
                sharedState.onMessageReceived(timestamp)
                Log.d(TAG, "Message received at $timestamp (unread=${sharedState.unreadCount.value})")
            }
        }
    }

    override fun onDestroy() {
        Log.i(TAG, "BadgeCounterService onDestroy")
    }

    private companion object {
        private const val TAG = "BadgeCounterService"
        private const val MESSAGE_INTERVAL_MS = 5_000L
    }
}
