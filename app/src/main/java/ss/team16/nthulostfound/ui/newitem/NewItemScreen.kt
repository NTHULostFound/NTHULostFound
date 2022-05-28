package ss.team16.nthulostfound.ui.newitem

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import kotlinx.coroutines.launch
import ss.team16.nthulostfound.model.NewItemType
import ss.team16.nthulostfound.ui.theme.NTHULostFoundTheme

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NewItemScreen(
    type: NewItemType,
    popScreen: () -> Unit,
    viewModel: NewItemViewModel = viewModel(factory = NewItemViewModelFactory(type, popScreen))
) {
    Scaffold(
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
        Column(
            Modifier
                .fillMaxSize()
                .padding(contentPadding)) {
            HorizontalPager(
                count = 4,
                state = viewModel.pagerState,
                // Add 32.dp horizontal padding to 'center' the pages
                contentPadding = PaddingValues(horizontal = 32.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                userScrollEnabled = false
            ) { page ->
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .weight(weight = 1f, fill = false)
                ) {
                    Text("Page $page")
                }
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