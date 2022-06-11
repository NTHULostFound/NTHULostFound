package ss.team16.nthulostfound.ui.itemdetail

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ss.team16.nthulostfound.domain.model.ItemType
import ss.team16.nthulostfound.ui.components.BackArrowAppBar
import ss.team16.nthulostfound.ui.components.IconLabel
import ss.team16.nthulostfound.ui.components.ImageCarousel
import ss.team16.nthulostfound.ui.components.InfoBox
import ss.team16.nthulostfound.utils.assistedViewModel
import java.util.*

val padding = 24.dp

@Composable
fun ItemDetailScreen(
    uuid: String,
    onBack: () -> Unit = { },
    navigateToRoute: (String) -> Unit = { },
    viewModel: ItemDetailViewModel = assistedViewModel {
        ItemDetailViewModel.provideFactory(
            itemDetailViewModelFactory(),
            uuid,
            navigateToRoute
        )
    }
) {
    Scaffold(
        topBar = {
            BackArrowAppBar(
                title = viewModel.item.name,
                onBack = onBack,
                onShare = {
                    viewModel.shareItem()
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
            if (viewModel.dialogState !is DialogState.Disabled) {
                ItemDetailScreenDialog(viewModel = viewModel)
            }

            ImageCarousel(
                modifier =
                    if (viewModel.item.images.isEmpty())
                        Modifier.aspectRatio(4/3f)
                    else
                        Modifier,
                networkImages = viewModel.item.images,
                shape = RectangleShape,
                borderWidth = 0.dp,
                borderColor = Color.Transparent
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(padding),
                modifier = Modifier
                    .padding(padding)
            ) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    IconLabel(
                        icon = Icons.Filled.Place,
                        labelText = viewModel.item.place,
                        modifier = Modifier.weight(1f)
                    )

                    val formatter = SimpleDateFormat("yyyy/M/d   h:mm a", Locale.getDefault())
                    val timeString = formatter.format(viewModel.item.date)

                    IconLabel(
                        icon = Icons.Outlined.AccessTime,
                        labelText = timeString
                    )
                }

                Text(
                    text = viewModel.item.description ?: "",
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(padding / 2),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text =
                        if (viewModel.item.type == ItemType.FOUND)
                            "如何取回"
                        else
                            "如何處理物品",
                        style = MaterialTheme.typography.h5
                    )

                    if (viewModel.item.how.isNotBlank()) {
                        Text(
                            text = viewModel.item.how
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1F))

                if (!viewModel.item.resolved) {
                    if (viewModel.viewMode == ViewMode.Owner) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(padding / 2),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            InfoBox(
                                info = "這是您新增的物品！\n"
                                        + "如果您已經成功${
                                            if (viewModel.item.type == ItemType.FOUND)
                                                "將失物交還給失主"
                                            else
                                                "尋回物品"
                                        }，您可以將其結案"
                            )

                            Button(
                                onClick = {
                                    viewModel.askEndItem()
                                },
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
                            info = "為避免騷擾或是濫用情形發生，如果按下「取得聯絡資訊」，將傳送通知給對方。"
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
                } else {
                    InfoBox(
                        info = "此物品已經成功結案！"
                    )
                }
            }
        }
    }
}

@Composable
fun ItemDetailScreenDialog(
    viewModel: ItemDetailViewModel
) {
    AlertDialog(
        onDismissRequest = { viewModel.onDialogDismiss() },
        confirmButton = {
            TextButton(onClick = { viewModel.onDialogConfirm() }) {
                Text(text = when(viewModel.dialogState) {
                    is DialogState.AskEnd -> "是"
                    else -> "確定"
                })
            }
        },
        dismissButton = {
            if (viewModel.dialogState is DialogState.AskEnd) {
                TextButton(onClick = { viewModel.onDialogDismiss() }) {
                    Text(text = when(viewModel.dialogState) {
                        is DialogState.AskEnd -> "否"
                        else -> "取消"
                    })
                }
            }
        },
        title = { Text(viewModel.dialogState.title) },
        text = {
            if (viewModel.dialogState is DialogState.ShowContact) {
                SelectionContainer() {
                    Text(viewModel.dialogState.text)
                }
            } else {
                Text(viewModel.dialogState.text)
            }
        }
    )
}