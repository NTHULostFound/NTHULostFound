package ss.team16.nthulostfound.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch
import ss.team16.nthulostfound.domain.model.ItemData
import ss.team16.nthulostfound.ui.components.*

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val showType = viewModel.showTypeFlow.collectAsState().value

    Scaffold(
        topBar = {
            val avatar = viewModel.avatarBitmap.collectAsState(initial = null).value

            HomeAppBar(
                navigateToRoute = { navController.navigate(it) },
                onSearch = { viewModel.onSearch(it) },
                avatar = avatar,
                isMyItems = viewModel.myItemsFlow.collectAsState().value,
                onMyItemsChanged = { viewModel.myItemsFlow.value = it }
            )
        },
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(it) { data ->
                Snackbar(
                    snackbarData = data,
//                    actionOnNewLine = true
                )
            }
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
            BottomNav(
                currentShowType = showType,
                modifier = modifier,
                onChangePage = { viewModel.onPageChanged(it) }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = modifier
                .padding(paddingValues)
        ) {
            // default: hide both before flow updated
            val showPinMessage = viewModel.showPinMessageFlow.collectAsState(initial = 0b00)
            val canShowPopUp = viewModel.canShowPopUpFlow.collectAsState(initial = false)
            val isMyItems = viewModel.myItemsFlow.collectAsState().value

            if (isMyItems) {
                AnimatedVisibility(
                    visible = isMyItems,
                    exit = slideOutVertically(
                        targetOffsetY = { -it },
                    ) + shrinkVertically()
                ) {
                    PinMessage(
                        message = "您正在瀏覽您的發佈記錄",
                        onClose = { viewModel.myItemsFlow.value = false }
                    )
                }
            } else if (showType == ShowType.FOUND) {
                AnimatedVisibility(
                    // check FOUND bit
                    visible = (showPinMessage.value and 0b10 != 0),
                    exit = slideOutVertically(
                        targetOffsetY = { -it },
                    ) + shrinkVertically()
                ) {
                    PinMessage(
                        message = "其他人撿到的遺失物將會刊登在這裡\n" +
                                "如果您遺失了物品，請從這裡尋找！",
                        onClose = { viewModel.onPinMessageClose() }
                    )
                }
            } else if (showType == ShowType.LOST) {
                AnimatedVisibility(
                    // check LOST bit
                    visible = (showPinMessage.value and 0b01 != 0),
                    exit = slideOutVertically(
                        targetOffsetY = { -it }
                    ) + shrinkVertically()
                ) {
                    PinMessage(
                        message = "這裡是失主刊登遺失物的地方\n" +
                                "如果您有發現這些物品，請與失主聯絡！",
                        onClose = { viewModel.onPinMessageClose() }
                    )
                }
            }

            val lazyState = rememberLazyListState()
            val lazyPagingItems = viewModel.items.collectAsLazyPagingItems()

            val isRefreshing = lazyPagingItems.loadState.refresh == LoadState.Loading
            val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { lazyPagingItems.refresh() },
                modifier = Modifier.fillMaxSize()
            ) {
                if (lazyPagingItems.loadState.refresh is LoadState.Error) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudOff,
                            contentDescription = "No Internet Connection",
                            modifier = Modifier
                                    .size(150.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "無網際網路連線，請連接 Wi-Fi 或是開啟行動數據。",
                            style = MaterialTheme.typography.body2
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        state = lazyState
                    ) {

                        if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
                            items(10) {
                                ItemCard(
                                    item = ItemData(
                                        name = "...",
                                        place = "...",
                                    ),
                                    modifier = Modifier.shimmer(),
                                    onClick = {}
                                )
                            }
                        }

                        itemsIndexed(lazyPagingItems) { _, item ->
                            if (item != null) {
                                ItemCard(item = item, onClick = {
                                    navController.navigate("item/${item.uuid}")
                                })
                            }
                        }

                        if (lazyPagingItems.loadState.append == LoadState.Loading) {
                            item {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }
                }
            }

            val isScrolledToEnd = remember(lazyState) {
                derivedStateOf {
                    lazyState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == lazyState.layoutInfo.totalItemsCount - 1
                }
            }
            if (isScrolledToEnd.value
                && lazyPagingItems.loadState.append.endOfPaginationReached
                && lazyPagingItems.loadState.append != LoadState.Loading
                && showType == ShowType.FOUND) {
                LaunchedEffect(Unit) {
                    launch {
                        val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                            message = "沒有找到您的失物？不要氣餒！立即新增協尋物品，讓找到的人能立刻聯繫到您！",
                            actionLabel = "前往新增"
                        )
                        when (snackbarResult) {
                            SnackbarResult.ActionPerformed -> {
                                navController.navigate("new_item/lost")
                            }
                            SnackbarResult.Dismissed -> {}
                        }
                    }
                }
            }
            else if(canShowPopUp.value) {
                LaunchedEffect(Unit) {
                    launch {
                        val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                            message = "您似乎尚未填寫個人資料，立即填寫以便在遺失物品時收到通知！",
                            actionLabel = "前往填寫"
                        )
                        when (snackbarResult) {
                            SnackbarResult.ActionPerformed -> {
                                navController.navigate("profile")
                            }
                            SnackbarResult.Dismissed -> {}
                        }
                        viewModel.onSetUserDataPopUp()
                    }
                }
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