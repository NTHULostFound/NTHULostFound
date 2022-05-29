package ss.team16.nthulostfound.ui.newitem

import android.view.KeyEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ss.team16.nthulostfound.model.NewItemType
import ss.team16.nthulostfound.ui.components.FormTextField
import ss.team16.nthulostfound.ui.components.ImageCarousel
import kotlin.math.roundToInt

@Composable
fun ConfirmPage(
    viewModel: NewItemViewModel
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var whoPosition  by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ImageCarousel(
            images = viewModel.imageBitmaps,
            padding = 16.dp
        )

        FormTextField(
            value = viewModel.name,
            label = "物品名稱",
            onValueChange = { viewModel.onNameChange(it) },
            icon = Icons.Outlined.WorkOutline,
            singleLine = true,
            required = true
        )

        FormTextField(
            value = viewModel.place,
            label =
            if (viewModel.type == NewItemType.NEW_FOUND)
                "拾獲地點"
            else
                "估略遺失地點",
            onValueChange = { viewModel.onPlaceChange(it) },
            icon = Icons.Outlined.Place,
            singleLine = true,
            required = true
        )

        FormTextField(
            value = viewModel.description,
            label = "物品詳細資訊",
            icon = Icons.Outlined.Info,
            onValueChange = { viewModel.onDescriptionChange(it) },
            singleLine = false,
            required = false
        )

        FormTextField(
            value = viewModel.how,
            label =
            if (viewModel.type == NewItemType.NEW_FOUND)
                "物品取回方式"
            else
                "物品處理方式",
            icon =
            if (viewModel.type == NewItemType.NEW_FOUND)
                Icons.Outlined.Undo
            else
                Icons.Outlined.HelpOutline,
            onValueChange = { viewModel.onHowChange(it) },
            singleLine = false,
            required = true
        )

        FormTextField(
            value = viewModel.contact,
            label = "聯繫方式",
            onValueChange = { viewModel.onContactChange(it) },
            icon = Icons.Outlined.ContactPage,
            singleLine = false,
            required = true,
            isLastField = !viewModel.whoEnabled
        )

        if (viewModel.type == NewItemType.NEW_FOUND) {
            WhoCheckBox(
                checked = viewModel.whoEnabled,
                onCheckedChange = { checked ->
                    viewModel.onWhoEnabledChange(checked)
                    if (checked)
                        scope.launch {
                            scrollState.animateScrollTo(whoPosition.roundToInt())
                        }
                }
            )

            AnimatedVisibility(visible = viewModel.whoEnabled) {
                FormTextField(
                    value = viewModel.who,
                    label = "失主的姓名或學號",
                    onValueChange = { viewModel.onWhoChange(it) },
                    icon = Icons.Outlined.Badge,
                    singleLine = true,
                    required = true,
                    isLastField = true,
                    onGloballyPositioned = { coordinates ->
                        whoPosition = coordinates.positionInRoot().y
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewItemConfirmText(
    value: String,
    label: String = "",
    onValueChange: (String) -> Unit,
    icon: ImageVector? = null,
    iconDescription: String? = label,
    singleLine: Boolean = true,
    required: Boolean = false,
    isLastField: Boolean = false,
    onGloballyPositioned: (LayoutCoordinates) -> Unit = {}
) {
    var isFirst by rememberSaveable { mutableStateOf(true) }
    val errorMessage =
        if (required && !isFirst && value.isEmpty())
            "請輸入${label}！"
        else
            null
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { onGloballyPositioned(it) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                isFirst = false
                onValueChange(it)
            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 16.dp)
                .onPreviewKeyEvent {
                    if (it.key == Key.Tab && it.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) {
                        focusManager.moveFocus(FocusDirection.Down)
                        true
                    } else
                        false
                },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction =
                if (isLastField)
                    ImeAction.Done
                else
                    ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
                onDone = { focusManager.clearFocus() }
            ),
            singleLine = singleLine,
            label = {
                Text(label)
            },
            leadingIcon =
            if (icon != null) {
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = iconDescription,
                        tint =
                        if (errorMessage != null)
                            MaterialTheme.colors.error
                        else
                            MaterialTheme.colors.onSurface
                    )
                }
            } else
                null,
            isError = errorMessage != null,
            trailingIcon =
            if (errorMessage != null) {
                {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = errorMessage
                    )
                }
            } else
                null
        )
    }
}

