package ss.team16.nthulostfound.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ss.team16.nthulostfound.R
import ss.team16.nthulostfound.ui.home.ShowType
import ss.team16.nthulostfound.ui.theme.NTHULostFoundTheme

enum class IndexTabs(
    @StringRes val title: Int,
    val icon: ImageVector,
    val showType: ShowType
) {
    FOUND(R.string.index_found_tab, Icons.Outlined.Search, ShowType.FOUND),
    LOST(R.string.index_lost_tab, Icons.Filled.Help, ShowType.LOST)
}

@Composable
fun BottomNav(
    currentShowType: ShowType,
    onChangePage: (ShowType) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = modifier
    ) {
        IndexTabs.values().forEach {
            val selected = it.showType == currentShowType

            BottomNavigationItem(
                icon = {
                    if (selected) {
                        Icon(
                            imageVector = it.icon,
                            contentDescription = stringResource(id = it.title),
                            // apply indicator
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colors.primaryVariant,
                                    shape = RoundedCornerShape(50)
                                )
                                .padding(vertical = 2.dp, horizontal = 12.dp)
                        )
                    } else {
                        Icon(
                            imageVector = it.icon,
                            contentDescription = stringResource(id = it.title)
                        )
                    }

                },
                label = {
                    Text(
                        text = stringResource(id = it.title)
                    )
                },
                selected = selected,
                onClick = {
                    if (it.showType != currentShowType)
                        onChangePage(it.showType)
                },
                modifier = Modifier
                    .padding()
                    .clip(RectangleShape)
            )
        }
    }
}

@Preview
@Composable
fun BottomNavPreview() {
    NTHULostFoundTheme {
        BottomNav(currentShowType = ShowType.FOUND, onChangePage = { })
    }
}