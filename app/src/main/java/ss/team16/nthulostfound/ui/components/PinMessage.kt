package ss.team16.nthulostfound.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PinMessage(
    modifier: Modifier = Modifier,
    message: String = "",
    onClose: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .background(MaterialTheme.colors.primaryVariant)
            .animateContentSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            Text(
                text = message,
                color = MaterialTheme.colors.onPrimary
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .width(48.dp)
                .padding(end = 16.dp)
        ) {
            IconButton(
                onClick = {
                    onClose()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "close pin message",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}