package ss.team16.nthulostfound.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ss.team16.nthulostfound.ui.theme.Shapes

enum class MultiFabState {
    Collapsed,
    Expanded
}

class MultiFabItem(
    val icon: ImageVector,
    val label: String,
    val srcIconColor: Color = Color.White,
    val labelTextColor: Color = Color.White,
    val labelBackgroundColor: Color = Color.Black.copy(alpha = 0.6F),
    val fabBackgroundColor: Color = Color.Unspecified,
    val onClick: () -> Unit
)

@Composable
fun MultiFloatingActionButton(
    modifier: Modifier = Modifier,
    srcIcon: ImageVector,
    srcIconColor: Color = MaterialTheme.colors.onPrimary,
    fabBackgroundColor: Color = MaterialTheme.colors.primary,
    showLabels: Boolean = true,
    wideButton: Boolean,
    wideButtonLabel: String,
    items: List<MultiFabItem>
) {
    val currentState = remember { mutableStateOf(MultiFabState.Collapsed) }
    val transition = updateTransition(targetState = currentState, label = "")
    val rotateAnim: Float by transition.animateFloat(
        transitionSpec = {
            if (targetState.value == MultiFabState.Expanded) {
                spring(stiffness = Spring.StiffnessLow)
            } else {
                spring(stiffness = Spring.StiffnessMedium)
            }
        }, label = ""
    ) { state ->
        if (state.value == MultiFabState.Collapsed) 0F else -45F
    }
    val alphaAnim: Float by transition.animateFloat(transitionSpec = {
        tween(durationMillis = 200)
    }, label = "") { state ->
        if (state.value == MultiFabState.Expanded) 1F else 0F
    }
    val shrinkListAnim:MutableList<Float> = mutableListOf()
    items.forEachIndexed { index, _ ->
        val shrinkAnim by transition.animateFloat(targetValueByState = { state ->
            when (state.value) {
                MultiFabState.Collapsed -> 5F
                MultiFabState.Expanded -> (index + 1) * 60F + if(index == 0) 5F else 0F
            }
        }, label = "", transitionSpec = {
            if (targetState.value == MultiFabState.Expanded) {
                spring(stiffness = Spring.StiffnessLow,dampingRatio = 0.58F)
            } else {
                spring(stiffness = Spring.StiffnessMedium)
            }
        })
        shrinkListAnim.add(index, shrinkAnim)
    }
    Box(modifier = modifier, contentAlignment = Alignment.BottomEnd) {
        items.forEachIndexed{index, item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(
                        bottom = shrinkListAnim[index].dp,
                        top = 5.dp,
                        end = 5.dp
                    )
                    .alpha(animateFloatAsState(alphaAnim).value)
            ) {
                if (showLabels) {
                    Text(
                        item.label,
                        color = item.labelTextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(Shapes.medium)
                            .alpha(animateFloatAsState(alphaAnim).value)
                            .background(color = item.labelBackgroundColor)
                            .padding(start = 6.dp, end = 6.dp, top = 4.dp, bottom = 4.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                FloatingActionButton(
                    backgroundColor = if (item.fabBackgroundColor == Color.Unspecified) MaterialTheme.colors.secondary else item.fabBackgroundColor,
                    modifier = Modifier.size(46.dp),
                    onClick = {
                        currentState.value = MultiFabState.Collapsed
                        item.onClick()
                    },
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 4.dp
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = item.icon,
                        tint = item.srcIconColor,
                        contentDescription = item.label
                    )
                }
            }
        }
        FloatingActionButton(
            backgroundColor = if(fabBackgroundColor == Color.Unspecified) MaterialTheme.colors.secondary else fabBackgroundColor,
            onClick = {
                currentState.value =
                    if (currentState.value == MultiFabState.Collapsed) MultiFabState.Expanded else MultiFabState.Collapsed
            }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp
                )
            ) {
                Icon(
                    imageVector = srcIcon,
                    modifier = Modifier.rotate(rotateAnim),
                    tint = srcIconColor,
                    contentDescription = null
                )

                AnimatedVisibility(
                    visible = wideButton && currentState.value == MultiFabState.Collapsed
                ) {
                    Row {
                        Spacer(Modifier.width(12.dp))
                        Text(wideButtonLabel, color = srcIconColor)
                    }
                }
            }
        }
    }
}