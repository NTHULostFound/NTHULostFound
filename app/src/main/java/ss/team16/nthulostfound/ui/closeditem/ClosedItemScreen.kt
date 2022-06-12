package ss.team16.nthulostfound.ui.closeditem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ss.team16.nthulostfound.domain.model.ItemType
import ss.team16.nthulostfound.ui.components.BackArrowAppBar
import ss.team16.nthulostfound.ui.newitem.padding

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ClosedItemScreen(
    onBack: () -> Unit = { },
    onAskReview: () -> Unit = {},
    viewModel: ClosedItemViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    Scaffold(
        topBar = {
            BackArrowAppBar(
                onBack = onBack,
                title = "結案",
                backEnabled = true
            )
        }
    ) { contentPadding ->

        LaunchedEffect(Unit) {
            launch {
                delay(1000)
                if (!viewModel.isReviewAsked.first()) {
                    bottomState.show()
                    viewModel.setReviewAsked()
                }
            }
        }


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
                    onClick = onBack
                ) {
                    Text(text = "完成")
                }
            }
        }
    }
    ModalBottomSheetLayout(
        sheetState = bottomState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetBackgroundColor = MaterialTheme.colors.background,
        sheetContent = {
            Column(
                modifier = Modifier
                    .padding(20.dp)
            ) {
                Text(
                    text = viewModel.askReviewTitle,
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = viewModel.askReviewMessage)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        when (viewModel.askReviewState) {
                            AskReviewState.FEEL -> {
                                viewModel.setReviewState(AskReviewState.BAD)
                            }
                            else -> {
                                scope.launch {
                                    bottomState.hide()
                                }
                            }
                        }
                    }) {
                        Text(viewModel.askReviewDismiss)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        when (viewModel.askReviewState) {
                            AskReviewState.FEEL -> {
                                viewModel.setReviewState(AskReviewState.GOOD)
                            }
                            AskReviewState.GOOD -> {
                                // review
                                onAskReview()
                                scope.launch {
                                    bottomState.hide()
                                }
                            }
                            AskReviewState.BAD -> {
                                // google form
                                viewModel.getFeedbackUseCase()
                                scope.launch {
                                    bottomState.hide()
                                }
                            }
                        }
                    }) {
                        Text(viewModel.askReviewConfirm)
                    }
                }
            }
        },
        content = {}
    )
}