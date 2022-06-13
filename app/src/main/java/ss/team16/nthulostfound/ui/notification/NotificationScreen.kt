package ss.team16.nthulostfound.ui.notification

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ss.team16.nthulostfound.ui.components.BackArrowAppBar
import ss.team16.nthulostfound.ui.components.NotificationItem

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onItemClicked: (String) -> Unit,
    viewModel: NotificationViewModel = hiltViewModel(),
) {
    val notifs = viewModel.notifs
    Scaffold(
        topBar = {
            BackArrowAppBar(
                onBack = onBack,
                title = "通知",
                backEnabled = true,
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier.padding(paddingValues = paddingValues)
        ) {
            items(items = notifs, key = {notif -> notif.id}) { notif ->
                NotificationItem(
                    data = notif,
                    onClick = {
                        onItemClicked(notif.itemUUID)
                        notif.read = true
                        viewModel.updateNotification(notif)
                    }
                )
            }
        }
    }
}

//@Preview
//@Composable
//fun NotificationPreview() {
//    val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.TAIWAN)
//    val notifs: List<NotificationData> = listOf(
//        NotificationData(
//            id = 1,
//            type = NotificationType.INSERTED,
//            content = "已成功新增協尋物品「宿舍鑰匙」。",
//            timestamp = formatter.parse("2022/05/30 20:00:13").time,
//            read = false,
//        ),
//        NotificationData(
//            id = 2,
//            type = NotificationType.CONTACT_CHECKED,
//            content = "有人在「書」查看了您的聯絡資訊！",
//            timestamp = formatter.parse("2022/05/30 12:34:56").time,
//            read = true,
//        ),
//        NotificationData(
//            id = 3,
//            type = NotificationType.LOST_NOTIFICATION,
//            content = "這是您遺失的「學生證」嗎？",
//            timestamp = formatter.parse("2022/05/29 22:07:19").time,
//            read = true,
//        )
//    )
//    NTHULostFoundTheme {
//        NotificationScreen(notifs)
//    }
//}