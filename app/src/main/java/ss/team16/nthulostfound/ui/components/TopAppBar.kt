package ss.team16.nthulostfound.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ss.team16.nthulostfound.R
import ss.team16.nthulostfound.ui.theme.NTHULostFoundTheme

@Composable
fun AppBar(
    title: String,
    currentRoute: String,
    navigateToRoute: (String) -> Unit,
    search: (String) -> Unit,
    modifier: Modifier = Modifier,
    avatar: Int? = null
) {
    var showSearchBar by rememberSaveable { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    TopAppBar(
        navigationIcon = {
            if (!showSearchBar) {
                when (currentRoute) {
                    "home/found", "home/lost" -> {
                        Image(
                            painter = painterResource(id = avatar ?: R.drawable.ic_appbar_avatar),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(all = 8.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable {
                                    navigateToRoute("profile")
                                }
                        )
                    }
                    else -> {
                        IconButton(onClick = {
                            // TODO: navigate back
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                }
            } else {
                IconButton(onClick = { showSearchBar = false }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        },
        title = {
            if (showSearchBar) {
                RoundedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it }
                )
            } else {
                Text(text = title)
            }
        },
        actions = {
              when  {
                  listOf("home/lost", "home/found").any { it == currentRoute } -> {
                      IconButton(onClick = {
                          if (!showSearchBar) showSearchBar = true
                          else search(searchQuery)
                      }) {
                          Icon(
                              imageVector = Icons.Filled.Search,
                              contentDescription = null
                          )
                      }
                      if (!showSearchBar) {
                          IconButton(onClick = {
                              // TODO: list self items, maybe done with search
                          }) {
                              Icon(
                                  imageVector = Icons.Filled.History,
                                  contentDescription = null
                              )
                          }
                          IconButton(onClick = {
                              navigateToRoute("notifications")
                          }) {
                              Icon(
                                  imageVector = Icons.Outlined.NotificationsNone,
                                  contentDescription = null
                              )
                          }
                      }
                  }
                  currentRoute.startsWith("item/") -> {
                      IconButton(onClick = {
                        // TODO: share onclick
                      }) {
                          Icon(
                              imageVector = Icons.Filled.Share,
                              contentDescription = null
                          )
                      }
                  }
              }
        },
        modifier = modifier,

    )
}

@Composable
fun RoundedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val enabled = true
    val singleLine = true
    val visualTransformation = VisualTransformation.None

    // TODO: apply NTHULostFoundTheme
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        singleLine = singleLine,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        textStyle = TextStyle(color = Color.White),
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        decorationBox = { innerTextField ->
          Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                  .padding(vertical = 8.dp, horizontal = 12.dp)
                  .fillMaxSize()
          ) {
              Box(Modifier.weight(1f)) {
                  if (value.isEmpty()) {
                      Text(
                          text = "搜尋",
                          color = Color.White,
                          fontSize = 14.sp
                      )
                  }
                  innerTextField()
              }
          }
        },
        modifier = modifier
            .padding(vertical = 4.dp)
            .fillMaxSize()
            .background(
                MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.BackgroundOpacity),
                RoundedCornerShape(50)
            )
    )
}

@Preview
@Composable
fun AppBarPreview() {
    NTHULostFoundTheme {
        AppBar(
            title = "test",
            currentRoute = "home/lost",
            navigateToRoute = { },
            search = { }
        )
    }
}