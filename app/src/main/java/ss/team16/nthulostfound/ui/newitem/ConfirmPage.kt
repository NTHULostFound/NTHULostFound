package ss.team16.nthulostfound.ui.newitem

import android.icu.text.SimpleDateFormat
import android.view.KeyEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.RectangleShape
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
import ss.team16.nthulostfound.domain.model.NewItemType
import ss.team16.nthulostfound.ui.components.FormTextField
import ss.team16.nthulostfound.ui.components.IconLabel
import ss.team16.nthulostfound.ui.components.ImageCarousel
import ss.team16.nthulostfound.ui.components.InfoBox
import ss.team16.nthulostfound.ui.itemdetail.ViewMode
import java.util.*
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun ConfirmPage(
    viewModel: NewItemViewModel
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ImageCarousel(
            modifier = Modifier.aspectRatio(4/3f),
            bitmapImages = viewModel.imageBitmaps,
            shape = RectangleShape,
            borderWidth = 0.dp,
            enableLightBox = true
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(padding),
            modifier = Modifier
                .padding(padding)
        ) {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconLabel(
                    icon = Icons.Filled.Place,
                    labelText = viewModel.place,
                    modifier = Modifier.weight(1f)
                )

                val cal = Calendar.getInstance()
                with(viewModel) {
                    cal.set(year, month, day, hour, minute)
                }

                val formatter = SimpleDateFormat("yyyy/M/d   h:mm a", Locale.getDefault())
                val timeString = formatter.format(cal.time)

                IconLabel(
                    icon = Icons.Outlined.AccessTime,
                    labelText = timeString
                )
            }

            Text(
                text = viewModel.name,
                style = MaterialTheme.typography.h4
            )

            Text(
                text = viewModel.description,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(padding / 2),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text =
                    if (viewModel.type == NewItemType.NEW_FOUND)
                        "物品取回方式"
                    else
                        "物品處理方式",
                    style = MaterialTheme.typography.h5
                )

                Text(
                    text = viewModel.how
                )
            }


            Column(
                verticalArrangement = Arrangement.spacedBy(padding / 2),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "聯繫方式",
                    style = MaterialTheme.typography.h5
                )

                Text(
                    text = viewModel.contact
                )
            }

            if (viewModel.whoEnabled) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(padding / 2),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "失主的姓名或學號",
                        style = MaterialTheme.typography.h5
                    )

                    Text(
                        text = viewModel.who
                    )
                }
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

