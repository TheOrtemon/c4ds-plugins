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
 * Session-scoped background service that increments an event counter every 5 seconds.
 *
 * Created at session start via [ToolDescriptor.createService]. Increments a shared counter
 * every ~5 s on a background coroutine while the service is alive; the coroutine is cancelled
 * automatically when [onDestroy] disposes [coroutineScope].
 *
 * Shared state is bound in [serviceModule] and exposed to the tool when it is activated.
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

    init {
        Log.i(TAG, "BadgeCounterService created — session service is running")
        coroutineScope.launch {
            while (isActive) {
                delay(TICK_INTERVAL_MS)
                val timestamp = SimpleDateFormat("HH:mm:ss", Locale.US).format(Date())
                sharedState.incrementEvent(timestamp)
                Log.d(TAG, "Service tick #${sharedState.eventCount.value} at $timestamp")
            }
        }
    }

    override fun onDestroy() {
        Log.i(TAG, "BadgeCounterService onDestroy")
    }

    private companion object {
        private const val TAG = "BadgeCounterService"
        private const val TICK_INTERVAL_MS = 5_000L
    }
}
