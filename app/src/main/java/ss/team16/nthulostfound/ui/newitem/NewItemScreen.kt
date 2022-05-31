package ss.team16.nthulostfound.ui.newitem

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import kotlinx.coroutines.launch
import ss.team16.nthulostfound.R
import ss.team16.nthulostfound.domain.model.NewItemType
import ss.team16.nthulostfound.ui.Greeting
import ss.team16.nthulostfound.ui.components.BackArrowAppBar
import ss.team16.nthulostfound.ui.theme.NTHULostFoundTheme

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NewItemScreen(
    type: NewItemType,
    popScreen: () -> Unit,
    viewModel: NewItemViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            val pageTitle =
                when (type) {
                    NewItemType.NEW_FOUND -> stringResource(R.string.title_new_found)
                    NewItemType.NEW_LOST -> stringResource(R.string.title_new_lost)
                }

            val backEnabled =
                viewModel.pagerState.currentPage != NewItemPageInfo.SENDING.value

            BackArrowAppBar(
                title = pageTitle,
                onBack = { popScreen() },
                backEnabled = backEnabled
            )
        },
        bottomBar = {
            // pagerState.animateScrollToPage has to be called in an compose coroutine scope
            // So the function has to be declared here and pass into the view model
            val scope = rememberCoroutineScope()
            val scrollToPage: (Int) -> Unit = { page: Int ->
                scope.launch {
                    viewModel.pagerState.animateScrollToPage(page)
                }
            }

            val contentResolver = LocalContext.current.contentResolver

            ViewPagerBar(
                pagerState = viewModel.pagerState,
                nextButtonInfo = viewModel.getPagerNextButtonInfo(),
                prevButtonInfo = viewModel.getPagerPrevButtonInfo(),
                onNextPage = { viewModel.goToNextPage(scrollToPage, contentResolver) },
                onPrevPage = { viewModel.goToPrevPage(scrollToPage) }
            )
        }
    ) { innerPadding ->
        HorizontalPager(
            count = 4,
            state = viewModel.pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            userScrollEnabled = false
        ) { page ->
            when (page) {
                0 -> { EditPage(viewModel = viewModel) }
                1 -> { ConfirmPage(viewModel = viewModel) }
                else -> {}
            }
        }
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun NewFoundItemPreview() {
//    NTHULostFoundTheme {
//        val navController = rememberNavController()
//        NavHost(
//            navController = navController,
//            startDestination = "new_item/found"
//        ) {
//            composable("new_item/{new_item_type}") {
//                NewItemScreen(
//                    type =
//                    if (it.arguments!!.getString("new_item_type") == "found")
//                        NewItemType.NEW_FOUND
//                    else
//                        NewItemType.NEW_LOST,
//                    popScreen = { navController.popBackStack() }
//                )
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun NewLostItemPreview() {
//    NTHULostFoundTheme {
//        NewItemScreen(NewItemType.NEW_LOST, {
//            Log.d(TAG, "Pop Screen")
//        })
//    }
//}