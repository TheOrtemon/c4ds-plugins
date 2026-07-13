package vision.combat.c4.ds.sample.gallery.storage.data

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import vision.combat.c4.ds.sample.gallery.storage.domain.repository.PreferencesStorageRepository
import vision.combat.c4.ds.sdk.data.util.observeAsStateFlow

/**
 * Thin SharedPreferences wrapper for the Preferences storage showcase.
 * The [SharedPreferences] instance is plugin-isolated — it is injected via Kodein
 * with `instance(arg = requireQualifiedName<StorageToolDescriptor>())`.
 */
internal class PreferencesStorageRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
) : PreferencesStorageRepository {
    override fun putString(value: String) {
        sharedPreferences.edit { putString(KEY_SAMPLE_STRING, value) }
    }

    override fun getString(): String =
        sharedPreferences.getString(KEY_SAMPLE_STRING, "") ?: ""

    override fun increment() {
        val next = sharedPreferences.getInt(KEY_COUNTER, 0) + 1
        sharedPreferences.edit { putInt(KEY_COUNTER, next) }
    }

    override fun observeString(scope: CoroutineScope): StateFlow<String> =
        sharedPreferences.observeAsStateFlow(KEY_SAMPLE_STRING, "", scope)

    override fun observeCounter(scope: CoroutineScope): StateFlow<Int> =
        sharedPreferences.observeAsStateFlow(KEY_COUNTER, 0, scope)

    private companion object {
        private const val KEY_SAMPLE_STRING = "sample_string"
        private const val KEY_COUNTER = "counter"
    }
}
