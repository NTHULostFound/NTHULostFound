package ss.team16.nthulostfound.ui.newitem

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.*

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ss.team16.nthulostfound.domain.model.NewItemType

@Composable
fun ResultPage(
    viewModel: NewItemViewModel
) {
    Crossfade(targetState = viewModel.uploadStatus) { status ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text =
                when (status) {
                    NewItemUploadStatus.IDLE,
                    NewItemUploadStatus.UPLOADING_IMAGE,
                    NewItemUploadStatus.UPLOADING_DATA
                    -> "上傳中..."
                    NewItemUploadStatus.DONE -> "新增完成"
                    NewItemUploadStatus.ERROR -> "發生錯誤"
                },
                style = MaterialTheme.typography.h3
            )

            StatusIcon(status = status)


            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text =
                    if (status == NewItemUploadStatus.DONE)
                        if (viewModel.type == NewItemType.NEW_FOUND)
                            "感謝您的熱心協助\n您好棒!"
                        else
                            "已經成功送出\n祝您順利尋回物品!"
                    else
                        viewModel.statusInfo,
                    style = MaterialTheme.typography.h5,
                    textAlign = TextAlign.Center
                )

                if (status == NewItemUploadStatus.ERROR) {
                    val contentResolver = LocalContext.current.contentResolver

                    TextButton(
                        onClick = { viewModel.submitForm(contentResolver) }
                    ) {
                        Icon(
                            Icons.Filled.RestartAlt,
                            contentDescription = "重新嘗試"
                        )
                        Text("重新嘗試")
                    }
                }

            }
        }
    }
}



@Composable
fun StatusIcon(
    status: NewItemUploadStatus
) {
    Box(
        Modifier
            .size(300.dp)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        if (status == NewItemUploadStatus.UPLOADING_IMAGE || status == NewItemUploadStatus.UPLOADING_DATA) {
            // Loading Animation Src:
            // https://github.com/mutualmobile/compose-animation-examples

            val infiniteTransition = rememberInfiniteTransition()
            val angle by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1600, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )

            Canvas(modifier = Modifier
                .size((150+20).dp), onDraw = {
                drawCircle(color = Color.DarkGray, style = Stroke(width = 20f))
            })

            Canvas(modifier = Modifier
                .size((150+20).dp), onDraw = {
                drawArc(
                    color =
                    Color(0xfff9d71c),
                    style = Stroke(
                        width = 20f,
                        cap = StrokeCap.Round,
                        join =
                        StrokeJoin.Round,
                    ),
                    startAngle = angle,
                    sweepAngle = 360 / 4f,
                    useCenter = false
                )
            })
        } else {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(
                        if (status == NewItemUploadStatus.DONE)
                            Color(0xFF2E7D32)
                        else
                            Color.Red
                    )
            )
        }

        Icon(
            imageVector =
            when (status) {
                NewItemUploadStatus.IDLE -> Icons.Filled.Image
                NewItemUploadStatus.UPLOADING_IMAGE -> Icons.Filled.Image
                NewItemUploadStatus.UPLOADING_DATA -> Icons.Filled.CloudUpload
                NewItemUploadStatus.DONE -> Icons.Filled.Done
                NewItemUploadStatus.ERROR -> Icons.Filled.PriorityHigh
            },
            contentDescription = null,
            tint =
            when (status) {
              NewItemUploadStatus.UPLOADING_DATA,
                NewItemUploadStatus.UPLOADING_IMAGE -> MaterialTheme.colors.onBackground
              else -> MaterialTheme.colors.onPrimary
            },
            modifier = Modifier.fillMaxSize(0.35F)
        )
    }
}