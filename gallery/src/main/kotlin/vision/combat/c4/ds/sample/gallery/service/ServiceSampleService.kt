package vision.combat.c4.ds.sample.gallery.service

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.kodein.di.DI
import vision.combat.c4.ds.sdk.tool.AbstractToolService
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Background service that increments an event counter every 5 seconds.
 *
 * `AbstractToolService` has no onStart/onStop hooks; work is started in init{}
 * and cancelled automatically when onDestroy() disposes coroutineScope.
 *
 * State is shared via [ServiceSharedState] created by [ServiceToolDescriptor].
 */
internal class ServiceSampleService(
    toolContext: ToolContext,
    descriptor: ToolDescriptor,
    di: DI,
    private val sharedState: ServiceSharedState,
) : AbstractToolService(toolContext, descriptor, di) {

    init {
        Log.i(TAG, "ServiceSampleService created — background service is running")
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
        Log.i(TAG, "ServiceSampleService onDestroy")
    }

    private companion object {
        private const val TAG = "ServiceSampleService"
        private const val TICK_INTERVAL_MS = 5_000L
    }
}
