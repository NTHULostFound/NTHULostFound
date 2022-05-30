package ss.team16.nthulostfound.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
                    startDestination = "new_item/found"
                ) {
                    composable("home/found") { Greeting(name = "found items") }
                    composable("home/lost") { Greeting(name = "lost items") }

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
