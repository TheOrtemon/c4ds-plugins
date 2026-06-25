package vision.combat.c4.ds.sample.gallery.service.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.service.ServiceSharedState
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar

@Composable
internal fun ServiceWindow(sharedState: ServiceSharedState) {
    WindowScaffold(
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.service_tool_name)) },
        content = { ServiceContent(sharedState) },
    )
}

@Composable
private fun ColumnScope.ServiceContent(sharedState: ServiceSharedState) {
    val eventCount by sharedState.eventCount.collectAsStateWithLifecycle()
    val lastEvent by sharedState.lastEventTime.collectAsStateWithLifecycle()

    Text(
        text = stringResource(R.string.service_running),
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 16.dp),
    )
    Card(elevation = 2.dp, modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            text = "${stringResource(R.string.service_events)} $eventCount",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(16.dp),
        )
    }
    Card(elevation = 2.dp) {
        Text(
            text = "${stringResource(R.string.service_last_event)} ${lastEvent ?: stringResource(R.string.service_none)}",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(16.dp),
        )
    }
}
