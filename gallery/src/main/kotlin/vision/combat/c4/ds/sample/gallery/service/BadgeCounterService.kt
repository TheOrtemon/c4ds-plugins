package vision.combat.c4.ds.sample.gallery.service

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.bindSingletonOf
import org.kodein.di.subDI
import vision.combat.c4.ds.sdk.tool.AbstractToolService
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Session-scoped service that is the single source of truth for this sample's state, mirroring how
 * host services back their badge from a session object (e.g. Chat/GeoFence read a shared interactor's
 * `totalUnreadCount`). Here the service owns that state directly, so there is no extra shared object:
 *
 * - **Inbox** — a background loop simulates a message arriving every few seconds, bumping
 *   [unreadCount] and prepending to [messageTimes]. [ServiceNotificationManager] maps [unreadCount]
 *   onto the tool-list badge, so the count shows on this tool's card while it is inactive.
 * - **Lifecycle log** — [lifecycleLog] records the tool's lifecycle callbacks. Because it lives on
 *   the session service (not the window), it survives the window being closed and reopened.
 *
 * The related [ServiceTool] reaches this instance straight from DI (`by instance()`): the SDK binds
 * every service into its tool's graph via `bindErasedInstance` in [AbstractToolService], so no
 * intermediate binding module is needed.
 */
internal class BadgeCounterService(
    toolContext: ToolContext,
    descriptor: ToolDescriptor,
    parentDI: DI,
) : AbstractToolService(toolContext, descriptor, parentDI) {

    override val di: DI = subDI(super.di) {
        bindSingletonOf(::ServiceNotificationManager)
    }

    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.US)

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    private val _messageTimes = MutableStateFlow<List<String>>(emptyList())
    val messageTimes: StateFlow<List<String>> = _messageTimes.asStateFlow()

    private val _lifecycleLog = MutableStateFlow<List<String>>(emptyList())
    val lifecycleLog: StateFlow<List<String>> = _lifecycleLog.asStateFlow()

    init {
        Log.i(TAG, "BadgeCounterService created — session service is running")
        coroutineScope.launch {
            while (isActive) {
                delay(MESSAGE_INTERVAL_MS)
                val timestamp = timeFormat.format(Date())
                _unreadCount.value++
                _messageTimes.value = (listOf(timestamp) + _messageTimes.value).take(MAX_MESSAGES)
                Log.d(TAG, "Message received at $timestamp (unread=${_unreadCount.value})")
            }
        }
    }

    /** Clears the unread badge (the tool-list badge hides once the count reaches 0). */
    fun markAllRead() {
        _unreadCount.value = 0
    }

    /** Records a tool lifecycle callback, timestamped, e.g. "12:00:03 — onComponentShown(Window)". */
    fun logLifecycle(event: String) {
        val entry = "${timeFormat.format(Date())} — $event"
        _lifecycleLog.value = (listOf(entry) + _lifecycleLog.value).take(MAX_LOG)
    }

    override fun onDestroy() {
        Log.i(TAG, "BadgeCounterService onDestroy")
    }

    private companion object {
        private const val TAG = "BadgeCounterService"
        private const val MESSAGE_INTERVAL_MS = 5_000L
        private const val MAX_MESSAGES = 5
        private const val MAX_LOG = 8
    }
}
