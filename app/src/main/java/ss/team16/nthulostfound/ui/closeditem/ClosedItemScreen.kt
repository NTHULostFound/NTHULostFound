package ss.team16.nthulostfound.ui.closeditem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ss.team16.nthulostfound.domain.model.ItemType
import ss.team16.nthulostfound.ui.components.BackArrowAppBar
import ss.team16.nthulostfound.ui.newitem.NewItemUploadStatus
import ss.team16.nthulostfound.ui.newitem.padding

@Composable
fun ClosedItemScreen(
    onBack: () -> Unit = { },
    navigateToRoute: (String) -> Unit = {},
    viewModel: ClosedItemViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            BackArrowAppBar(
                onBack = onBack,
                title = "結案",
                backEnabled = true
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(rememberScrollState())
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "已結案",
                style = MaterialTheme.typography.h3
            )

            Box(
                Modifier
                    .size(250.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2E7D32))
                )

                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "closed case",
                    tint = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.fillMaxSize(0.5F)
                )
            }

            if (viewModel.type == ItemType.LOST) {
                Text(
                    textAlign = TextAlign.Center,
                    text = "很高興您找到了「${viewModel.name}」！\n" +
                            "恭喜你這次遇到了好心人，\n" +
                            "動動手指，立刻加入他們的行列吧！"
                )
            } else if (viewModel.type == ItemType.FOUND) {
                Text(
                    textAlign = TextAlign.Center,
                    text = "很感謝您幫助失主找回了「${viewModel.name}」！\n" +
                            "感謝你此次的協助，動動手指，再接再厲吧！"
                )
            }

            Button(
                onClick = {
                    viewModel.shareResult()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Filled.Share, contentDescription = "share")
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "分享您的喜悅")
            }

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    border = BorderStroke(0.dp, Color.Transparent),
                    elevation = null,
                    onClick = {
                        navigateToRoute("item/lost")
                    }
                ) {
                    Text(text = "完成")
                }
            }
        }
    }
}