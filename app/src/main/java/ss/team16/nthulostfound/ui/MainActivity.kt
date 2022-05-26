package ss.team16.nthulostfound.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import ss.team16.nthulostfound.ui.theme.NTHULostFoundTheme

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
                    composable("home/found") { Greeting(name = "found items") }
                    composable("home/lost") { Greeting(name = "lost items") }
                    composable("profile") { Greeting(name = "profile") }
                    composable("new_item/found") { Greeting(name = "new found") }
                    composable("new_item/lost") { Greeting(name = "new lost") }
                    composable("item/{itemId}") {
                        val itemId = it.arguments?.getString("itemId")!!
                        Greeting(name = itemId)
                    }
                    composable("item/{itemId}/contact") {
                        val itemId = it.arguments?.getString("itemId")!!
                        Greeting(name = "$itemId's contact")
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
