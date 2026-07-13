package vision.combat.c4.ds.sample.gallery.window.multiscreen.domain.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

/**
 * Domain-facing contract for the Multi Screen Window showcase's settings storage. The
 * concrete storage mechanism (SharedPreferences) is a Data-layer implementation detail —
 * Domain only knows about this interface.
 */
internal interface WindowMultiScreenRepository {
    fun setShowDescription(show: Boolean)

    fun observeShowDescription(scope: CoroutineScope): StateFlow<Boolean>
}
