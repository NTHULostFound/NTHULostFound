package ss.team16.nthulostfound.ui.newitem

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import ss.team16.nthulostfound.domain.model.NewItemType
import ss.team16.nthulostfound.ui.components.IconLabel
import ss.team16.nthulostfound.ui.components.ImageCarousel
import java.util.*

@Composable
fun ConfirmPage(
    viewModel: NewItemViewModel
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ImageCarousel(
            modifier = Modifier.aspectRatio(4/3f),
            bitmapImages = viewModel.imageBitmaps,
            shape = RectangleShape,
            borderWidth = 0.dp,
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
                    labelText = viewModel.place,
                    modifier = Modifier.weight(1f)
                )

                val cal = Calendar.getInstance()
                with(viewModel) {
                    cal.set(year, month, day, hour, minute)
                }

                val formatter = SimpleDateFormat("yyyy/M/d   h:mm a", Locale.getDefault())
                val timeString = formatter.format(cal.time)

                IconLabel(
                    icon = Icons.Outlined.AccessTime,
                    labelText = timeString
                )
            }

            Text(
                text = viewModel.name,
                style = MaterialTheme.typography.h4
            )

            Text(
                text = viewModel.description,
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
                    if (viewModel.type == NewItemType.NEW_FOUND)
                        "物品取回方式"
                    else
                        "物品處理方式",
                    style = MaterialTheme.typography.h5
                )

                Text(
                    text = viewModel.how
                )
            }


            Column(
                verticalArrangement = Arrangement.spacedBy(padding / 2),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "聯繫方式",
                    style = MaterialTheme.typography.h5
                )

                Text(
                    text = viewModel.contact
                )
            }

            if (viewModel.whoEnabled) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(padding / 2),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "失主的姓名或學號",
                        style = MaterialTheme.typography.h5
                    )

                    Text(
                        text = viewModel.who
                    )
                }
            }
        }
    }
}
