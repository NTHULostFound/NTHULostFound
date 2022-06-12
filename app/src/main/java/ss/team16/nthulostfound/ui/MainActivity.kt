package ss.team16.nthulostfound.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode
import com.google.android.play.core.review.testing.FakeReviewManager
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent
import ss.team16.nthulostfound.domain.model.NewItemType
import ss.team16.nthulostfound.ui.closeditem.ClosedItemScreen
import ss.team16.nthulostfound.ui.home.HomeScreen
import ss.team16.nthulostfound.ui.itemdetail.ItemDetailScreen
import ss.team16.nthulostfound.ui.itemdetail.ItemDetailViewModel
import ss.team16.nthulostfound.ui.newitem.NewItemScreen
import ss.team16.nthulostfound.ui.newitem.NewItemViewModel
import ss.team16.nthulostfound.ui.notification.NotificationScreen
import ss.team16.nthulostfound.ui.profile.ProfileScreen
import ss.team16.nthulostfound.ui.theme.NTHULostFoundTheme

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
                val localContext = LocalContext.current
                val reviewManager = ReviewManagerFactory.create(localContext)
                var reviewInfo: ReviewInfo? = rememberReviewTask(reviewManager = reviewManager)
                reviewManager.requestReviewFlow().addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        reviewInfo = task.result
                    }
                }

                LaunchedEffect(true) {
                    handleFirebaseDynamicLinks(this@MainActivity.intent, navController)
                }

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        HomeScreen(
                            navController = navController
                        )
                    }

                    composable("new_item/found") {
                        NewItemScreen(
                            type = NewItemType.NEW_FOUND,
                            popScreen = { navController.popBackStack() },
                            navigateToItem = { uuid ->
                                navController.navigate("item/$uuid") {
                                    popUpTo("home")
                                }
                            }
                        )
                    }
                    composable("new_item/lost") {
                        NewItemScreen(
                            type = NewItemType.NEW_LOST,
                            popScreen = { navController.popBackStack() },
                            navigateToItem = { uuid ->
                                navController.navigate("item/$uuid") {
                                    popUpTo("home")
                                }
                            }
                        )
                    }

                    composable(
                        "item/{itemId}",
                        arguments = listOf(navArgument("itemId") {
                            type = NavType.StringType
                            defaultValue = ""
                        }),
                        deepLinks = listOf(
                            navDeepLink {
                                uriPattern = "nthulostfound://item/{itemId}"
                        })
                    ) {
                        val itemId = it.arguments?.getString("itemId")!!
                        ItemDetailScreen(
                            uuid = itemId,
                            onBack = { navController.popBackStack() },
                            navigateToRoute = { route -> navController.navigate(route) {
                                // we pop up to home before navigate to closed item screen
                                // so we can just call onBack in closed item screen for "done" button
                                popUpTo("home") {
                                    if (route == "home")
                                        inclusive = true
                                }
                            } }
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
                            onBack = {
                                navController.popBackStack()
                            },
                            onAskReview = {
                                reviewInfo?.let {
                                    Log.d("Review", "launch review")
                                    reviewManager.launchReviewFlow(localContext as Activity, it)
                                }
                            }
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

private fun handleFirebaseDynamicLinks(intent: Intent, navController: NavController) {
    Firebase.dynamicLinks
        .getDynamicLink(intent)
        .addOnSuccessListener { linkData ->
            linkData?.link?.let { uri ->
                val itemId = uri.getQueryParameter("id")
                itemId?.let { id ->
                    navController.navigate("item/$id")
                }
            }
        }
        .addOnFailureListener { e -> Log.w("DynamicLink", "getDynamicLink:onFailure", e) }
}

@Composable
fun rememberReviewTask(reviewManager: ReviewManager): ReviewInfo? {
    var reviewInfo: ReviewInfo? by remember {
        mutableStateOf(null)
    }
    reviewManager.requestReviewFlow().addOnCompleteListener {
        if (it.isSuccessful) {
            reviewInfo = it.result
        }
    }

    return reviewInfo
}