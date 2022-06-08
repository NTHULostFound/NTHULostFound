package ss.team16.nthulostfound.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent
import ss.team16.nthulostfound.domain.model.NewItemType
import ss.team16.nthulostfound.ui.closeditem.ClosedItemScreen
import ss.team16.nthulostfound.ui.home.HomeScreen
import ss.team16.nthulostfound.ui.home.HomeViewModel
import ss.team16.nthulostfound.ui.home.ShowType
import ss.team16.nthulostfound.ui.itemdetail.ItemDetailScreen
import ss.team16.nthulostfound.ui.itemdetail.ItemDetailViewModel
import ss.team16.nthulostfound.ui.newitem.NewItemScreen
import ss.team16.nthulostfound.ui.newitem.NewItemViewModel
import ss.team16.nthulostfound.ui.notification.NotificationScreen
import ss.team16.nthulostfound.ui.profile.ProfileScreen
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

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NTHULostFoundTheme {
                val navController = rememberAnimatedNavController()

                AnimatedNavHost(
                    navController = navController,
                    startDestination = "home",
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None },
                ) {
                    composable("home") {
                        HomeScreen(
                            navController = navController
                        )
                    }

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
                            defaultValue = "bd5234b5-35b5-4faf-9c01-684819077461"
                        })
                    ) {
                        val itemId = it.arguments?.getString("itemId")!!
                        ItemDetailScreen(
                            uuid = itemId,
                            onBack = { navController.popBackStack() },
                            navigateToRoute = { route -> navController.navigate(route) }
                        )
                    }
                    composable("item/{itemId}/contact") {
                        val itemId = it.arguments?.getString("itemId")!!
                        Greeting(name = "$itemId's contact")
                    }
                    composable(
                        "closed_item?itemType={itemType}&itemName={itemName}",
                        arguments = listOf(
                            navArgument("itemType") {
                                type = NavType.StringType
                                defaultValue = "found"
                            },
                            navArgument("itemName") {
                                type = NavType.StringType
                                defaultValue = ""
                            }
                        )
                    ) {
                        ClosedItemScreen(
                            onBack = { navController.popBackStack() },
                            navigateToRoute = { route -> navController.navigate(route) }
                        )
                    }

                    composable("profile") { 
                        ProfileScreen(onBack = { navController.popBackStack() })
                    }
                    composable("notifications") {
                        NotificationScreen(
                            onBack = { navController.popBackStack() },
                            onItemClicked = { uuid ->
                                navController.navigate("item/$uuid")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}
