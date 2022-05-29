package ss.team16.nthulostfound.ui.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ss.team16.nthulostfound.R
import ss.team16.nthulostfound.model.UserData
import ss.team16.nthulostfound.ui.components.BackArrowAppBar
import ss.team16.nthulostfound.ui.components.FormField
import ss.team16.nthulostfound.ui.components.FormTextField
import ss.team16.nthulostfound.ui.components.InfoBox
import ss.team16.nthulostfound.ui.theme.NTHULostFoundTheme

val padding = 24.dp

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory())
) {
    Scaffold(
        topBar = {
            BackArrowAppBar(
                onBack = onBack,
                title = "個人檔案",
                backEnabled = true
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(padding)
        ) {
            val avatarModifier = Modifier
                .fillMaxWidth(.75F)
                .clip(CircleShape)
                .clickable {
                    Log.i("Profile", "avatar clicked")
                }

            if (viewModel.user.avatar != null) {
                Image(
                    bitmap = viewModel.user.avatar!!.asImageBitmap(),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = avatarModifier
                )
            } else {
                // fallback avatar
                Image(
                    painter = painterResource(id = R.drawable.ic_appbar_avatar),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = avatarModifier
                )
            }

            FormTextField(
                value = viewModel.user.name,
                onValueChange = {},
                label = "姓名"
            )

            FormTextField(
                value = viewModel.user.studentId,
                onValueChange = {},
                label = "學號"
            )

            FormTextField(
                value = viewModel.user.email,
                onValueChange = {},
                label = "E-mail"
            )

            InfoBox(
                info = "如果有人撿到與您資訊相符的物品，APP 將會傳送推播通知及 E-mail 給您！",
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "notifications icon",
                    modifier = Modifier.padding(end = 2.dp)
                )
                Text(
                    text = "開啟通知",
                    style = MaterialTheme.typography.h6
                )
                // fill space so the switch would be and the end of the row
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = viewModel.notificationEnabled,
                    onCheckedChange = { status -> viewModel.enableNotification(status) },
                )
            }
        }
    }
}

@Preview
@Composable
fun ProfilePreview() {
    NTHULostFoundTheme {
        ProfileScreen(
            onBack = {},
            viewModel = viewModel(factory = ProfileViewModelFactory(
                UserData(
                    null,
                    "",
                    "",
                    "なまえ",
                    "109000000",
                    "nthu@example.com"
                )
            ))
        )
    }
}