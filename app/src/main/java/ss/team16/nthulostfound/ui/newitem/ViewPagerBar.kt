package ss.team16.nthulostfound.ui.newitem

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ViewPagerBar(
    pagerState: PagerState,
    nextButtonInfo: PagerButtonInfo?,
    prevButtonInfo: PagerButtonInfo?,
    onNextPage: () -> Unit,
    onPrevPage: () -> Unit
) {
    Surface(
        color = MaterialTheme.colors.primarySurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(bottom = 8.dp)
                    .padding(horizontal = 8.dp)
            ) {


                if (prevButtonInfo != null)
                    TextButton(
                        onClick = onPrevPage,
                        enabled = prevButtonInfo.enabled,
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = MaterialTheme.colors.primarySurface,
                            contentColor = contentColorFor(MaterialTheme.colors.primarySurface)
                        )
                    ) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = prevButtonInfo.label
                        )
                        Text(prevButtonInfo.label)
                    }
                else
                    Box(modifier = Modifier)

                if (nextButtonInfo != null)
                    TextButton(
                        onClick = onNextPage,
                        enabled = nextButtonInfo.enabled,
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = MaterialTheme.colors.primarySurface,
                            contentColor = contentColorFor(MaterialTheme.colors.primarySurface)
                        )
                    ) {
                        Text(nextButtonInfo.label)
                        Icon(
                            Icons.Filled.ArrowForward,
                            contentDescription = nextButtonInfo.label
                        )
                    }
                else
                    Box(modifier = Modifier)
            }
        }
    }
}