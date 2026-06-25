package vision.combat.c4.ds.sample.gallery.window.navigation.data

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import vision.combat.c4.ds.sdk.data.util.observeAsStateFlow

internal class WindowNavRepository(
    private val sharedPreferences: SharedPreferences,
) {
    fun getOpenOnTop(): Boolean =
        sharedPreferences.getBoolean(KEY_OPEN_ON_TOP, false)

    fun setOpenOnTop(openOnTop: Boolean) {
        sharedPreferences.edit { putBoolean(KEY_OPEN_ON_TOP, openOnTop) }
    }

    fun observeOpenOnTop(scope: CoroutineScope): StateFlow<Boolean> =
        sharedPreferences.observeAsStateFlow(KEY_OPEN_ON_TOP, false, scope)

    private companion object {
        private const val KEY_OPEN_ON_TOP = "open_on_top"
    }
}

