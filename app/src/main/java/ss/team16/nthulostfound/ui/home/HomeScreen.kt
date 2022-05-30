package ss.team16.nthulostfound.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import ss.team16.nthulostfound.ui.Greeting
import ss.team16.nthulostfound.ui.components.BottomNav
import ss.team16.nthulostfound.ui.components.HomeAppBar
import ss.team16.nthulostfound.ui.theme.NTHULostFoundTheme

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
        Column(
            modifier = modifier.padding(paddingValues = paddingValues)
        ) {
            if(showType != null)
                Greeting(name = showType)
            else
                Greeting(name = "null")
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