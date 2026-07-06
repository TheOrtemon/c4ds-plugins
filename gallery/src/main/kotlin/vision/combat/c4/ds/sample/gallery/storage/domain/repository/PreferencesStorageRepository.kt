package vision.combat.c4.ds.sample.gallery.storage.domain.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

/**
 * Domain-facing contract for the Preferences storage showcase. The concrete storage
 * mechanism (SharedPreferences) is a Data-layer implementation detail — Domain only
 * knows about this interface.
 */
internal interface PreferencesStorageRepository {
    fun putString(value: String)

    fun getString(): String

    fun increment()

    fun observeString(scope: CoroutineScope): StateFlow<String>

    fun observeCounter(scope: CoroutineScope): StateFlow<Int>
}
