package ss.team16.nthulostfound.ui.newitem

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import kotlinx.coroutines.launch
import ss.team16.nthulostfound.R
import ss.team16.nthulostfound.model.NewItemType
import ss.team16.nthulostfound.ui.components.BackArrowAppBar
import ss.team16.nthulostfound.ui.components.ImageCarousel
import ss.team16.nthulostfound.ui.theme.NTHULostFoundTheme

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NewItemScreen(
    type: NewItemType,
    popScreen: () -> Unit,
    viewModel: NewItemViewModel = viewModel(factory = NewItemViewModelFactory(type, popScreen))
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

            ViewPagerBar(
                pagerState = viewModel.pagerState,
                nextButtonInfo = viewModel.getPagerNextButtonInfo(),
                prevButtonInfo = viewModel.getPagerPrevButtonInfo(),
                onNextPage = { viewModel.goToNextPage(scrollToPage) },
                onPrevPage = { viewModel.goToPrevPage(scrollToPage) }
            )
        }
    ) { contentPadding ->
        HorizontalPager(
            count = 4,
            state = viewModel.pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            userScrollEnabled = false
        ) { page ->
            when (page) {
                0 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(all = 8.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        ImageCarousel(
                            images = viewModel.imageBitmaps,
                            padding = 16.dp,
                            addImage = true,
                            onAddImage = { uri, context ->
                                viewModel.onAddImage(uri, context)
                            },
                            deleteButton = true,
                            onDeleteImage = { index ->
                                viewModel.onDeleteImage(index)
                            }
                        )
                    }
                }
                else -> {}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewItemScreenPreview() {
    NTHULostFoundTheme {
        NewItemScreen(NewItemType.NEW_FOUND, {
            Log.d("NewItemScreen", "Pop Screen")
        })
    }
}