package vision.combat.c4.ds.sample.gallery.service

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolNotificationManager
import vision.combat.c4.ds.sdk.ui.platform.IntentProvider

/**
 * Minimal [ToolNotificationManager] that mirrors the service tick counter on the tool list badge.
 */
internal class ServiceNotificationManager(
    context: ToolContext,
    intentProvider: IntentProvider,
    sharedState: ServiceSharedState,
) : ToolNotificationManager(context, intentProvider) {

    override val counter: StateFlow<UInt> = sharedState.eventCount
        .map { count -> count.toUInt() }
        .stateIn(coroutineScope, SharingStarted.Eagerly, 0u)

    override fun onDestroy() = Unit
}
