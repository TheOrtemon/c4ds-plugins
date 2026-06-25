package vision.combat.c4.ds.sample.gallery.service

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Shared state between [ServiceSampleService] and [ServiceWindow].
 * The descriptor creates one instance and passes it to both.
 */
internal class ServiceSharedState {
    private val _eventCount = MutableStateFlow(0)
    val eventCount: StateFlow<Int> = _eventCount.asStateFlow()

    private val _lastEventTime = MutableStateFlow<String?>(null)
    val lastEventTime: StateFlow<String?> = _lastEventTime.asStateFlow()

    fun incrementEvent(timestamp: String) {
        _eventCount.value++
        _lastEventTime.value = timestamp
    }
}

