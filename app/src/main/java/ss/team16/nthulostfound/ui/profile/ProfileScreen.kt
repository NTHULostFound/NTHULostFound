package ss.team16.nthulostfound.ui.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import ss.team16.nthulostfound.R
import ss.team16.nthulostfound.domain.model.UserData
import ss.team16.nthulostfound.domain.usecase.ChangeAvatarUseCase
import ss.team16.nthulostfound.ui.components.BackArrowAppBar
import ss.team16.nthulostfound.ui.components.FormTextField
import ss.team16.nthulostfound.ui.components.InfoBox
import ss.team16.nthulostfound.ui.theme.NTHULostFoundTheme
import ss.team16.nthulostfound.utils.assistedViewModel
import java.io.File

val padding = 24.dp

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val isNotificationEnable = viewModel.isNotificationEnable.collectAsState(initial = true)

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

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent(),
                onResult = { uri -> viewModel.onChangeAvatar(uri) }
            )

            val avatarModifier = Modifier
                .fillMaxWidth(.75F)
                .aspectRatio(1f)
                .clip(CircleShape)
                .clickable {
                    launcher.launch("image/*")
                }

            val avatar = viewModel.avatarBitmap.collectAsState(initial = null)

            if (avatar.value != null) {
                Image(
                    bitmap = avatar.value!!.asImageBitmap(),
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

            Column(
                verticalArrangement = Arrangement.spacedBy(padding / 2),
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                FormTextField(
                    value = viewModel.user.name,
                    onValueChange = {
                        viewModel.onTextFieldChange("name", it)
                    },
                    label = "姓名"
                )
                FormTextField(
                    value = viewModel.user.studentId,
                    onValueChange = {
                        viewModel.onTextFieldChange("studentId", it)
                    },
                    label = "學號"
                )
                FormTextField(
                    value = viewModel.user.email,
                    onValueChange = {
                        viewModel.onTextFieldChange("email", it)
                    },
                    label = "E-mail"
                )

                AnimatedVisibility(
                    visible = viewModel.hasChangedTextFieldValue,
                    enter = fadeIn(animationSpec = tween(durationMillis = 250)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 250))
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(padding / 2, Alignment.End),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = {
                                focusManager.clearFocus(true)
                                viewModel.resetUser()
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                backgroundColor = Color.Transparent
                            ),
                            enabled = !viewModel.submitDisabled
                        ) {
                            Text("取消變更")
                        }
                        Button(
                            onClick = {
                                focusManager.clearFocus(true)
                                viewModel.saveUser()
                            },
                            enabled = !viewModel.submitDisabled
                        ) {
                            Text("儲存")
                        }
                    }
                }
            }

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
                    checked = isNotificationEnable.value,
                    onCheckedChange = { status -> viewModel.setEnableNotification(status) },
                )
            }

            InfoBox(
                info = "按下按鈕後，將會重設首頁說明訊息（預設為顯示）。",
            )
            
            OutlinedButton(
                onClick = {
                    viewModel.resetPinMessage()
                    Toast.makeText(
                        context,
                        "重設完成！",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("重設首頁說明訊息")
            }
        }
    }
}
