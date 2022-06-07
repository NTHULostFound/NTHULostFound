package ss.team16.nthulostfound.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val items = viewModel.items
    val showTypeStr = if(viewModel.showType == ShowType.FOUND) "found" else "lost"
    Scaffold(
        topBar = {
            HomeAppBar(navigateToRoute = {
                navController.navigate(it)
            }, onSearch = {

            })
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                when (viewModel.fabState) {
                    FabState.WITH_TEXT -> {
                        ExtendedFloatingActionButton(
                            text = {
                                Text("新增")
                            },
                            icon = {
                                Icon(
                                    imageVector =
                                    if(viewModel.fabState != FabState.EXTENDED) {
                                        Icons.Filled.Add
                                    } else {
                                        Icons.Filled.Close
                                    },
                                    contentDescription = "add"
                                )
                            },
                            onClick = {
                                viewModel.onFabClicked()
                            }
                        )
                    }
                    FabState.EXTENDED -> {
                        ExtendedFloatingActionButton(
                            onClick = { navController.navigate("new_item/lost") },
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.Help,
                                    contentDescription = "add_lost"
                                )
                            },
                            text = {
                                Text(
                                    text = "我東西掉了",
                                    modifier = modifier.width(96.dp)
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        ExtendedFloatingActionButton(
                            onClick = { navController.navigate("new_item/found") },
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "add_found"
                                )
                            },
                            text = {
                                Text(
                                    text = "我撿到東西了",
                                    modifier = modifier.width(96.dp)
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        FloatingActionButton(onClick = { viewModel.onFabClicked() }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "close"
                            )
                        }
                    }
                    FabState.COLLAPSED -> {
                        FloatingActionButton(onClick = { viewModel.onFabClicked() }) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "add"
                            )
                        }
                    }
                }

            }
        },
        bottomBar = {
            BottomNav(
                currentRoute = "home/$showTypeStr",
                modifier = modifier,
                navigateToRoute = {
                    if(it != "home/$showTypeStr")
                        navController.navigate(it)
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