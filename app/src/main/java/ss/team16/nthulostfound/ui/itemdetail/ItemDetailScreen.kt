package ss.team16.nthulostfound.ui.itemdetail

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.ui.components.BackArrowAppBar
import ss.team16.nthulostfound.ui.components.ImageCarousel
import ss.team16.nthulostfound.ui.components.InfoBox
import ss.team16.nthulostfound.ui.newitem.NewItemViewModel
import ss.team16.nthulostfound.ui.theme.NTHULostFoundTheme
import ss.team16.nthulostfound.utils.assistedViewModel
import java.util.*

val padding = 24.dp

@Composable
fun ItemDetailScreen(
    viewMode: ViewMode,
    uuid: String,
    onBack: () -> Unit = { },
    viewModel: ItemDetailViewModel = assistedViewModel {
        ItemDetailViewModel.provideFactory(
            itemDetailViewModelFactory(),
            viewMode,
            uuid
        )
    }
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            BackArrowAppBar(
                title = viewModel.item.name,
                onBack = onBack,
                onShare = {
                    viewModel.shareItem(context)
                },
                backEnabled = true,
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
        ) {
            if (viewModel.showDialog) {
                AlertDialog(
                    onDismissRequest = { viewModel.setDialogStatus(false) },
                    confirmButton = {
                        TextButton(onClick = { viewModel.setDialogStatus(false) }) {
                            Text("Ok")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.setDialogStatus(false) }) {
                            Text("Dismiss")
                        }
                    },
                    title = { Text("sad") },
                    text = { Text("sadder") }
                )
            }

            ImageCarousel(
                images = emptyList(),
                shape = RectangleShape,
                borderWidth = 0.dp
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(padding),
                modifier = Modifier
                    .padding(padding)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    IconLabel(
                        icon = Icons.Filled.Place,
                        labelText = viewModel.item.place
                    )

                    val formatter = SimpleDateFormat("yyyy/M/d   h:mm a", Locale.getDefault())
                    val timeString = formatter.format(viewModel.item.date)

                    IconLabel(
                        icon = Icons.Outlined.AccessTime,
                        labelText = timeString
                    )
                }

                Text(
                    text = viewModel.item.description,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(padding / 2),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "如何取回",
                        style = MaterialTheme.typography.h5
                    )

                    Text(
                        text = viewModel.item.how
                    )
                }

                Spacer(modifier = Modifier.weight(1F))

                if (viewModel.viewMode == ViewMode.Owner) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(padding / 2),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                backgroundColor = Color.Black
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Filled.Delete, contentDescription = "delete")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "刪除")
                        }

                        Button(
                            onClick = { },
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = "finish")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "結案")
                        }
                    }
                } else if (viewModel.viewMode == ViewMode.Guest) {
                    InfoBox(
                        info = "為避免騷擾或是濫用情形發生，如果按下「顯示聯絡資訊」，將傳送通知給對方。"
                    )

                    Button(
                        onClick = {
                            viewModel.getContact()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Filled.ContactPage, contentDescription = "get contact info")
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = "取得聯絡資訊")
                    }
                }
            }
        }
    }
}

@Composable
fun IconLabel(
    icon: ImageVector,
    labelText: String = "",
    modifier: Modifier = Modifier
){
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Icon(imageVector = icon, contentDescription = labelText)
        Text(text = labelText)
    }
}

//@Preview
//@Composable
//fun ItemDetailPreview(){
//    NTHULostFoundTheme {
//        ItemDetailScreen(
//            onBack = {},
//            viewModel = ItemDetailViewModel(
//                ViewMode.Guest,
//                ItemData(
//                    "機率課本",
//                "我的機率課本不見了，可能是上完課忘記帶走了，但我回去找之後就找不到了",
//                    Date(),
//                    "台達 105",
//                    "請連絡我取回"
//                )
//            )
//        )
//    }
//}