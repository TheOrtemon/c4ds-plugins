package vision.combat.c4.ds.sample.gallery.service

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolNotificationManager
import vision.combat.c4.ds.sdk.ui.platform.IntentProvider

/**
 * Minimal [ToolNotificationManager] that mirrors the inbox unread count onto the tool-list badge.
 *
 * [counter] is the public API consumed by the SDK to update the badge numeral displayed
 * next to this tool in the tool list. It is derived from [ServiceSharedState.unreadCount]
 * via a lightweight `map + stateIn(Eagerly)` so the badge stays current even while the
 * tool window is closed. The SDK renders the badge only while the count is greater than 0 and
 * the tool is inactive, so "Mark all read" (which zeroes the count) makes the badge disappear.
 */
internal class ServiceNotificationManager(
    context: ToolContext,
    intentProvider: IntentProvider,
    sharedState: ServiceSharedState,
) : ToolNotificationManager(context, intentProvider) {

    override val counter: StateFlow<UInt> = sharedState.unreadCount
        .map { count -> count.toUInt() }
        .stateIn(coroutineScope, SharingStarted.Eagerly, 0u)

    override fun onDestroy() = Unit
}
