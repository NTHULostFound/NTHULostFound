package ss.team16.nthulostfound.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import ss.team16.nthulostfound.R
import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.ui.theme.NTHULostFoundTheme
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ItemCard(
    modifier: Modifier = Modifier,
    item: ItemData
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.LightGray, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
    ) {
        Row() {
            Column(
                modifier = modifier
                    .weight(3F)
                    .padding(12.dp)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.h5,
                    modifier = modifier.padding(4.dp)
                )

                Text(
                    text = item.description ?: "",
                    style = MaterialTheme.typography.body1,
                    modifier = modifier.padding(4.dp)
                )
            }
            Column(
                modifier = modifier
                    .weight(2F)
                    .padding(12.dp)
            ) {
                val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN)
                IconLabel(
                    icon = Icons.Outlined.AccessTime,
                    labelText = formatter.format(item.date),
                    modifier = modifier.padding(4.dp)
                )
                IconLabel(
                    icon = Icons.Outlined.Place,
                    labelText = item.place,
                    modifier = modifier.padding(4.dp)
                )
            }
        }

        if(item.images.isNotEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.image_book),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ItemCardPreview() {
    val item: ItemData = ItemData(
        name = "機率課本",
        description = "我的機率課本不見了，可能是上完課忘記帶走了，但我回去找之後就找不到了",
        date = Date(),
        place = "台達 105",
        how = "請連絡我取回"
    )
    NTHULostFoundTheme {
        ItemCard(item = item)
    }
}