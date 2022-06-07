package ss.team16.nthulostfound.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ss.team16.nthulostfound.ui.components.*

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            HomeAppBar(navigateToRoute = {
                navController.navigate(it)
            }, onSearch = {

            })
        },
        floatingActionButton = {
            val expandFabItemList: MutableList<MultiFabItem> = mutableListOf(
                MultiFabItem(
                    icon = Icons.Filled.Search,
                    label = "我撿到東西了",
                    fabBackgroundColor = Color.Black,
                    onClick = { navController.navigate("new_item/found") }
                ),
                MultiFabItem(
                    icon = Icons.Filled.Help,
                    label = "我東西掉了",
                    fabBackgroundColor = Color.Black,
                    onClick = { navController.navigate("new_item/lost") }
                )
            )
            MultiFloatingActionButton(
                srcIcon = Icons.Filled.Add,
                items = expandFabItemList)
        },
        bottomBar = {
            val showTypeStr = if(viewModel.showType == ShowType.FOUND) "found" else "lost"
            BottomNav(
                currentRoute = "home/$showTypeStr",
                modifier = modifier,
                navigateToRoute = {
                    if(it != "home/$showTypeStr") {
                        viewModel.onShowTypeChanged()
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .padding(paddingValues),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val items = viewModel.items
            items(items) { item ->
                ItemCard(item = item, onClick = {
                    navController.navigate("item/${item.uuid}")
                })
            }
        }
    }
}


//@Preview
//@Composable
//fun HomeScreenPreview() {
//    NTHULostFoundTheme {
//        HomeScreen(null)
//    }
//}