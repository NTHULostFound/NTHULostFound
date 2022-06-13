package ss.team16.nthulostfound.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import ss.team16.nthulostfound.R
import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.ui.theme.NTHULostFoundTheme
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ItemCardShimmer(
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 8.dp
    ) {
        val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.Window)
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            Row {
                Column(
                    modifier = modifier
                        .weight(3F)
                        .padding(12.dp)
                ) {
                    Box(
                        modifier =
                            Modifier
                                .padding(4.dp)
                                .shimmer(shimmerInstance)
                                .background(Color.LightGray)
                    ) {
                        Text(
                            text = "Loading..........",
                            style = MaterialTheme.typography.h5,
                            color = Color.Transparent
                        )
                    }


                    Box(
                        modifier =
                        Modifier
                            .padding(4.dp)
                            .shimmer(shimmerInstance)
                            .background(Color.LightGray)
                    ) {
                        Text(
                            text = "Loading.............",
                            style = MaterialTheme.typography.body1,
                            color = Color.Transparent
                        )
                    }
                }
                Column(
                    modifier = modifier
                        .weight(2F)
                        .padding(12.dp)
                ) {
                    Box(
                        modifier =
                        Modifier
                            .shimmer(shimmerInstance)
                            .padding(4.dp)
                            .background(Color.LightGray)
                    ) {
                        Text(
                            text = "Loading...........",
                            color = Color.Transparent
                        )
                    }

                    Box(
                        modifier =
                        Modifier
                            .shimmer(shimmerInstance)
                            .padding(4.dp)
                            .background(Color.LightGray)
                    ) {
                        Text(
                            text = "Loading...........",
                            color = Color.Transparent
                        )
                    }
                }
            }
            Box(
                modifier
                    .fillMaxSize()
                    .aspectRatio(4/3f)
                    .shimmer(shimmerInstance)
                    .background(Color.LightGray)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ItemCardShimmerPreview() {
    NTHULostFoundTheme {
        ItemCardShimmer()
    }
}