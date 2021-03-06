package ss.team16.nthulostfound.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ss.team16.nthulostfound.ui.theme.NTHULostFoundTheme

@Composable
fun BackArrowAppBar(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    backEnabled: Boolean = false,
    title: String = "",
    onShare: (() -> Unit)? = null
) {
    TopAppBar(
        backgroundColor = if (!isSystemInDarkTheme()) MaterialTheme.colors.primary
                          else MaterialTheme.colors.secondary,
        navigationIcon = {
            IconButton(onClick = onBack, enabled = backEnabled) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "back"
                )
            }
        },
        title = {
            Text( text = title )
        },
        actions = {
            if (onShare != null) {
                IconButton(onClick = onShare) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "share",
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun BackArrowAppBarPreview() {
    NTHULostFoundTheme {
        BackArrowAppBar(
            title = "Preview",
            onBack = { },
            onShare = { }
        )
    }
}