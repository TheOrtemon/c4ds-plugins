package vision.combat.c4.ds.sample.gallery.window.multiscreen.data

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import vision.combat.c4.ds.sdk.data.util.observeAsStateFlow

internal class WindowMultiScreenRepository(
    private val sharedPreferences: SharedPreferences,
) {
    fun setShowDescription(show: Boolean) {
        sharedPreferences.edit { putBoolean(KEY_SHOW_DESCRIPTION, show) }
    }

    fun observeShowDescription(scope: CoroutineScope): StateFlow<Boolean> =
        sharedPreferences.observeAsStateFlow(KEY_SHOW_DESCRIPTION, true, scope)

    private companion object {
        private const val KEY_SHOW_DESCRIPTION = "show_description"
    }
}
