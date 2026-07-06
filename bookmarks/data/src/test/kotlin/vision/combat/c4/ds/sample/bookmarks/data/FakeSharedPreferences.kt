package vision.combat.c4.ds.sample.bookmarks.data

import android.content.SharedPreferences

/**
 * Minimal in-memory [SharedPreferences] fake for unit tests. Only the members exercised by
 * [BookmarkRepositoryImpl] (and by the SDK's `observeAsStateFlow` extension) are implemented;
 * everything else throws so unexpected usage fails loudly instead of behaving unpredictably.
 */
class FakeSharedPreferences : SharedPreferences {

    private val values = mutableMapOf<String, Any?>()
    private val listeners = mutableSetOf<SharedPreferences.OnSharedPreferenceChangeListener>()

    override fun getAll(): MutableMap<String, *> = values.toMutableMap()

    override fun getString(key: String, defValue: String?): String? =
        if (values.containsKey(key)) values[key] as? String else defValue

    override fun getStringSet(key: String, defValues: MutableSet<String>?): MutableSet<String>? =
        if (values.containsKey(key)) {
            @Suppress("UNCHECKED_CAST")
            (values[key] as? Set<String>)?.toMutableSet()
        } else {
            defValues
        }

    override fun getInt(key: String, defValue: Int): Int =
        if (values.containsKey(key)) values[key] as? Int ?: defValue else defValue

    override fun getLong(key: String, defValue: Long): Long = throw NotImplementedError()

    override fun getFloat(key: String, defValue: Float): Float = throw NotImplementedError()

    override fun getBoolean(key: String, defValue: Boolean): Boolean = throw NotImplementedError()

    override fun contains(key: String): Boolean = values.containsKey(key)

    override fun edit(): SharedPreferences.Editor = FakeEditor()

    override fun registerOnSharedPreferenceChangeListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener,
    ) {
        listeners += listener
    }

    override fun unregisterOnSharedPreferenceChangeListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener,
    ) {
        listeners -= listener
    }

    private inner class FakeEditor : SharedPreferences.Editor {
        private val pendingUpdates = mutableMapOf<String, Any?>()
        private val pendingRemovals = mutableSetOf<String>()
        private var clearAll = false

        override fun putString(key: String, value: String?): SharedPreferences.Editor = apply {
            pendingUpdates[key] = value
            pendingRemovals -= key
        }

        override fun putStringSet(key: String, values: MutableSet<String>?): SharedPreferences.Editor = apply {
            pendingUpdates[key] = values?.toSet()
            pendingRemovals -= key
        }

        override fun putInt(key: String, value: Int): SharedPreferences.Editor = apply {
            pendingUpdates[key] = value
            pendingRemovals -= key
        }

        override fun putLong(key: String, value: Long): SharedPreferences.Editor = throw NotImplementedError()

        override fun putFloat(key: String, value: Float): SharedPreferences.Editor = throw NotImplementedError()

        override fun putBoolean(key: String, value: Boolean): SharedPreferences.Editor = throw NotImplementedError()

        override fun remove(key: String): SharedPreferences.Editor = apply {
            pendingRemovals += key
            pendingUpdates -= key
        }

        override fun clear(): SharedPreferences.Editor = apply {
            clearAll = true
        }

        override fun commit(): Boolean {
            apply()
            return true
        }

        override fun apply() {
            if (clearAll) {
                values.clear()
            }
            pendingRemovals.forEach { key -> values.remove(key) }
            pendingUpdates.forEach { (key, value) -> values[key] = value }

            val changedKeys = pendingUpdates.keys + pendingRemovals
            listeners.toSet().forEach { listener ->
                changedKeys.forEach { key -> listener.onSharedPreferenceChanged(this@FakeSharedPreferences, key) }
            }
        }
    }
}
