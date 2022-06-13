package ss.team16.nthulostfound.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CircleNotifications
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.PermContactCalendar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ss.team16.nthulostfound.R
import ss.team16.nthulostfound.domain.model.NotificationData
import ss.team16.nthulostfound.domain.model.NotificationType
import ss.team16.nthulostfound.ui.theme.NTHULostFoundTheme
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun NotificationItem(
    modifier: Modifier = Modifier,
    data: NotificationData,
    onClick: () -> Unit
) {
    val iconType = when(data.type) {
        NotificationType.LOST_NOTIFICATION -> Icons.Filled.Help
        NotificationType.CONTACT_CHECKED -> Icons.Filled.PermContactCalendar
        NotificationType.LOST_INSERTED -> Icons.Filled.CheckCircle
        NotificationType.FOUND_INSERTED -> Icons.Filled.CheckCircle
        NotificationType.UNSPECIFIED -> Icons.Filled.CircleNotifications
    }
    val notifString = when(data.type) {
        NotificationType.LOST_NOTIFICATION -> stringResource(R.string.lost_notification_message, data.content)
        NotificationType.CONTACT_CHECKED -> stringResource(R.string.contact_checked_message, data.content)
        NotificationType.LOST_INSERTED -> stringResource(R.string.lost_inserted_message, data.content)
        NotificationType.FOUND_INSERTED -> stringResource(R.string.found_inserted_message, data.content)
        NotificationType.UNSPECIFIED -> data.content
    }

    val date = Date(data.timestamp)
    val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
    val color = if(data.read) {
        MaterialTheme.colors.surface
    } else {
        MaterialTheme.colors.primary.copy(alpha = 0.55f)
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = color)
            .clickable {
                onClick()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(top = 20.dp, start = 8.dp)
        ) {
            Icon(
                imageVector = iconType,
                contentDescription = "",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
            Text(
                text = notifString,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
            )
        }
        Text(
            text = formatter.format(date),
            style = MaterialTheme.typography.caption,
            modifier = modifier
                .align(Alignment.End)
                .padding(end = 8.dp, bottom = 4.dp)
        )
        Divider()
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationItemPreview() {
    NTHULostFoundTheme {
        val data = NotificationData(
            id = 1,
            type = NotificationType.CONTACT_CHECKED,
            content = "宿舍鑰匙",
            timestamp = Date().time,
            read = false,
        )
        NotificationItem(data = data, onClick = {})
    }
}