package ss.team16.nthulostfound.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent
import ss.team16.nthulostfound.domain.model.NewItemType
import ss.team16.nthulostfound.ui.home.HomeScreen
import ss.team16.nthulostfound.ui.itemdetail.ItemDetailScreen
import ss.team16.nthulostfound.ui.itemdetail.ItemDetailViewModel
import ss.team16.nthulostfound.ui.itemdetail.ViewMode
import ss.team16.nthulostfound.ui.newitem.NewItemScreen
import ss.team16.nthulostfound.ui.newitem.NewItemViewModel
import ss.team16.nthulostfound.ui.theme.NTHULostFoundTheme
import ss.team16.nthulostfound.utils.assistedViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface ViewModelFactoryProvider {
        fun newItemViewModelFactory(): NewItemViewModel.Factory
        fun itemDetailViewModelFactory(): ItemDetailViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NTHULostFoundTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "home/{show_type}"
                ) {
                    composable("home/{show_type}",
                        arguments = listOf(navArgument("show_type") {
                            type = NavType.StringType
                            defaultValue = "found"
                        })
                    ) {
//                        LaunchedEffect(Unit) {
//                            navController.navigate("new_item/found")
//                        }

                        HomeScreen(navController, showType = it.arguments?.getString("show_type"))
                    }
//                    composable("home/lost") {
//
//                        Greeting(name = "lost items")
//                    }
                    composable("new_item/found") {
                        NewItemScreen(
                            type = NewItemType.NEW_FOUND,
                            popScreen = { navController.popBackStack() }
                        )
                    }

                    composable("new_item/lost") {
                        NewItemScreen(
                            type = NewItemType.NEW_LOST,
                            popScreen = { navController.popBackStack() }
                        )
                    }

                    composable(
                        "item/{itemId}",
                        arguments = listOf(navArgument("itemId") {
                            type = NavType.StringType
                            defaultValue = ""
                        })
                    ) {
                        val itemId = it.arguments?.getString("itemId")!!
                        ItemDetailScreen(viewMode = ViewMode.Guest, uuid = itemId)
                    }
                    composable("item/{itemId}/contact") {
                        val itemId = it.arguments?.getString("itemId")!!
                        Greeting(name = "$itemId's contact")
                    }
                    composable("item/closed") { Greeting(name = "item closed") }

                    composable("profile") { Greeting(name = "profile") }
                    composable("notifications") { Greeting(name = "notifications") }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}
