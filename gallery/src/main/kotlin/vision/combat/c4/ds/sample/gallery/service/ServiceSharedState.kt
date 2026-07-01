package vision.combat.c4.ds.sample.gallery.service

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Session-scoped state shared between [BadgeCounterService] and
 * [vision.combat.c4.ds.sample.gallery.service.ui.ServiceWindow]. Bound as a singleton in
 * [vision.combat.c4.ds.sample.gallery.service.di.serviceModule], so both the background service
 * and the (repeatedly created/destroyed) tool window observe the same instance.
 *
 * Holds two things the sample demonstrates:
 * - an **inbox**: [unreadCount] drives the tool-list badge; [messageTimes] lists recent arrivals.
 * - a **lifecycle log**: [lifecycleLog] records the tool's lifecycle callbacks. Because it lives
 *   here — in the session service — and not in the window, it survives the window being closed.
 */
internal class ServiceSharedState {

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    private val _messageTimes = MutableStateFlow<List<String>>(emptyList())
    val messageTimes: StateFlow<List<String>> = _messageTimes.asStateFlow()

    private val _lifecycleLog = MutableStateFlow<List<String>>(emptyList())
    val lifecycleLog: StateFlow<List<String>> = _lifecycleLog.asStateFlow()

    /** A background message arrived: bump the unread badge and prepend its timestamp. */
    fun onMessageReceived(timestamp: String) {
        _unreadCount.value++
        _messageTimes.value = (listOf(timestamp) + _messageTimes.value).take(MAX_MESSAGES)
    }

    /** Clears the unread badge (the tool-list badge hides once the count reaches 0). */
    fun markAllRead() {
        _unreadCount.value = 0
    }

    /** Prepends a formatted tool lifecycle entry (e.g. "12:00:03 — onComponentShown(Window)"). */
    fun logLifecycle(entry: String) {
        _lifecycleLog.value = (listOf(entry) + _lifecycleLog.value).take(MAX_LOG)
    }

    private companion object {
        const val MAX_MESSAGES = 5
        const val MAX_LOG = 8
    }
}
