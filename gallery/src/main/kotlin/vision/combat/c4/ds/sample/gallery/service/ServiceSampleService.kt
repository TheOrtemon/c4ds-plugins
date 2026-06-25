package vision.combat.c4.ds.sample.gallery.service

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.DI
import vision.combat.c4.ds.sdk.tool.AbstractToolService
import vision.combat.c4.ds.sdk.tool.ToolContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Background service that increments an event counter every 5 seconds.
 * State is shared via [ServiceSharedState] created by [ServiceToolDescriptor].
 */
internal class ServiceSampleService(
    toolContext: ToolContext,
    di: DI,
    private val sharedState: ServiceSharedState,
) : AbstractToolService(toolContext, di) {

    override fun onStart() {
        Log.i(TAG, "ServiceSampleService onStart — autoStart service is running")
        scope.launch {
            while (true) {
                delay(TICK_INTERVAL_MS)
                val timestamp = SimpleDateFormat("HH:mm:ss", Locale.US).format(Date())
                sharedState.incrementEvent(timestamp)
                Log.d(TAG, "Service tick #${sharedState.eventCount.value} at $timestamp")
            }
        }
    }

    override fun onStop() {
        Log.i(TAG, "ServiceSampleService onStop")
    }

    private companion object {
        private const val TAG = "ServiceSampleService"
        private const val TICK_INTERVAL_MS = 5_000L
    }
}
