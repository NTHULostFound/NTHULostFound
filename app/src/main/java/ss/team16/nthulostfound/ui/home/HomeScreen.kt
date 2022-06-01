package ss.team16.nthulostfound.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.domain.model.UploadedImage
import ss.team16.nthulostfound.ui.components.BottomNav
import ss.team16.nthulostfound.ui.components.HomeAppBar
import ss.team16.nthulostfound.ui.components.ItemCard
import ss.team16.nthulostfound.ui.theme.NTHULostFoundTheme
import java.util.*

@Composable
fun HomeScreen(
    navController: NavController?,
    modifier: Modifier = Modifier,
    showType: String? = "found"
) {
    Scaffold(
        topBar = {
            HomeAppBar(navigateToRoute = {
                navController!!.navigate(it)
            }, onSearch = {

            })
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                       Text("新增")
                },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "add"
                    )
               },
                onClick = {

                }
            )
        },
        bottomBar = {
            BottomNav(
                currentRoute = "home/${showType}",
                modifier = modifier,
                navigateToRoute = {
                    navController!!.navigate(it)
                }
            )
        }
    ) { paddingValues ->
        val itemList = listOf(
            ItemData(
                "書",
                "好像是機率的書",
                Date(),
                "台達 105",
                "請聯繫我取回 啾咪"
            ),
            ItemData(
                "書",
                "好像是機率的書",
                Date(),
                "台達 105",
                "請聯繫我取回 啾咪"
            ),
            ItemData(
                "書",
                "好像是機率的書",
                Date(),
                "台達 105",
                "請聯繫我取回 啾咪",
                listOf(UploadedImage("https://example.com"))
            ),
            ItemData(
                "書",
                "好像是機率的書",
                Date(),
                "台達 105",
                "請聯繫我取回 啾咪",
                listOf(UploadedImage("https://example.com"))
            ),
            ItemData(
                "書",
                "好像是機率的書",
                Date(),
                "台達 105",
                "請聯繫我取回 啾咪",
                listOf(UploadedImage("https://example.com"))
            )
        )
        LazyColumn(
            modifier = modifier
                .padding(paddingValues)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(itemList) { item ->
                ItemCard(item = item)
            }
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    NTHULostFoundTheme {
        HomeScreen(null)
    }
}