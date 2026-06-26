package vision.combat.c4.ds.sample.gallery.window.multiscreen

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import vision.combat.c4.ds.sample.gallery.window.multiscreen.data.WindowMultiScreenRepository

internal class WindowMultiScreenInteractor(
    private val repository: WindowMultiScreenRepository,
) {
    fun observeShowDescription(scope: CoroutineScope): StateFlow<Boolean> =
        repository.observeShowDescription(scope)

    fun setShowDescription(show: Boolean) {
        repository.setShowDescription(show)
    }
}
