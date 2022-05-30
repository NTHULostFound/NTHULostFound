package ss.team16.nthulostfound.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import dagger.hilt.android.AndroidEntryPoint
import ss.team16.nthulostfound.domain.model.NewItemType
import ss.team16.nthulostfound.ui.newitem.NewItemScreen
import ss.team16.nthulostfound.ui.theme.NTHULostFoundTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NTHULostFoundTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "home/found"
                ) {
                    composable("home/found") {
                        LaunchedEffect(Unit) {
                            navController.navigate("new_item/found")
                        }
                    }
                    composable("home/lost") { Greeting(name = "lost items") }

                    composable("new_item/{new_item_type}") {
                        val type = it.arguments!!.get("new_item_type")
                        NewItemScreen(
                            type =
                            if (type == "found")
                                NewItemType.NEW_FOUND
                            else
                                NewItemType.NEW_LOST,
                            popScreen = { navController.popBackStack() }
                        )
                    }

                    composable("item/{itemId}") {
                        val itemId = it.arguments?.getString("itemId")!!
                        Greeting(name = itemId)
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
