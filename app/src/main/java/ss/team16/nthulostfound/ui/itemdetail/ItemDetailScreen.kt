package ss.team16.nthulostfound.ui.itemdetail

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material.icons.filled.Delete
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
                modifier = Modifier.aspectRatio(4/3f),
                networkImages = viewModel.item.images,
                shape = RectangleShape,
                borderWidth = 0.dp,
                borderColor = Color.Transparent,
                enableLightBox = true
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
                            "????????????"
                        else
                            "??????????????????",
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
                                info = "???????????????????????????\n"
                                        + "?????????????????????${
                                            if (viewModel.item.type == ItemType.FOUND)
                                                "????????????????????????"
                                            else
                                                "????????????"
                                        }????????????????????????"
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
                                Text(text = "??????")
                            }

                            val deleteButtonBackgroundColor = if (!isSystemInDarkTheme()) Color.Black else MaterialTheme.colors.surface
                            Button(
                                onClick = {
                                  viewModel.askDeleteItem()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = Color.White,
                                    backgroundColor = deleteButtonBackgroundColor
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Icon(imageVector = Icons.Filled.Delete, contentDescription = "delete")
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "??????")
                            }
                        }
                    } else if (viewModel.viewMode == ViewMode.Guest) {
                        InfoBox(
                            info = "????????????????????????????????????????????????????????????????????????????????????????????????????????????"
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
                            Text(text = "??????????????????")
                        }
                    }
                } else {
                    if (viewModel.viewMode == ViewMode.Guest) {
                        InfoBox(
                            info = "??????????????????????????????"
                        )
                    } else if (viewModel.viewMode == ViewMode.Owner) {
                        InfoBox(
                            info = "??????????????????????????????\n????????????????????????????????????"
                        )

                        Button(
                            onClick = {
                                viewModel.askDeleteItem()
                            },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                backgroundColor = Color.Black
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Filled.Delete, contentDescription = "delete")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "??????")
                        }
                    }

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
                    is DialogState.AskEnd, is DialogState.AskDelete -> "???"
                    else -> "??????"
                })
            }
        },
        dismissButton = {
            if (
                viewModel.dialogState is DialogState.AskEnd ||
                viewModel.dialogState is DialogState.AskDelete
            ) {
                TextButton(onClick = { viewModel.onDialogDismiss() }) {
                    Text(text = when(viewModel.dialogState) {
                        is DialogState.AskEnd, is DialogState.AskDelete -> "???"
                        else -> "??????"
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