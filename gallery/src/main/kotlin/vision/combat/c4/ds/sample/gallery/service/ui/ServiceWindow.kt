package vision.combat.c4.ds.sample.gallery.service.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.service.BadgeCounterService
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar

@Composable
internal fun ServiceWindow(service: BadgeCounterService) {
    WindowScaffold(
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.service_tool_name)) },
        content = { ServiceContent(service) },
    )
}

@Composable
private fun ColumnScope.ServiceContent(service: BadgeCounterService) {
    val unread by service.unreadCount.collectAsStateWithLifecycle()
    val messages by service.messageTimes.collectAsStateWithLifecycle()
    val lifecycleLog by service.lifecycleLog.collectAsStateWithLifecycle()

    // ── Service status ──
    Text(
        text = stringResource(R.string.service_running),
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 4.dp),
    )
    Text(
        text = stringResource(R.string.service_status_caption),
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(bottom = 16.dp),
    )

    // ── Inbox / badge ──
    Card(elevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.service_inbox_title),
                    style = MaterialTheme.typography.subtitle1,
                )
                Text(
                    text = stringResource(R.string.service_unread, unread),
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (messages.isEmpty()) {
                Text(
                    text = stringResource(R.string.service_inbox_empty),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                )
            } else {
                messages.forEach { timestamp ->
                    Text(
                        text = stringResource(R.string.service_message, timestamp),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.padding(vertical = 2.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { service.markAllRead() },
                enabled = unread > 0,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.service_mark_read))
            }
        }
    }
    Text(
        text = stringResource(R.string.service_inbox_caption),
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp),
    )

    // ── Tool lifecycle log ──
    Card(elevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.service_lifecycle_title),
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(bottom = 12.dp),
            )
            if (lifecycleLog.isEmpty()) {
                Text(
                    text = stringResource(R.string.service_lifecycle_empty),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                )
            } else {
                lifecycleLog.forEach { entry ->
                    Text(
                        text = entry,
                        style = MaterialTheme.typography.body2,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.padding(vertical = 2.dp),
                    )
                }
            }
        }
    }
    Text(
        text = stringResource(R.string.service_lifecycle_caption),
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(top = 4.dp),
    )
}
