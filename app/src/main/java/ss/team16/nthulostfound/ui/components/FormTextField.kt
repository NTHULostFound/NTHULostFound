package ss.team16.nthulostfound.ui.components

import android.view.KeyEvent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FormTextField(
    value: String,
    label: String = "",
    onValueChange: (String) -> Unit,
    icon: ImageVector? = null,
    iconDescription: String? = label,
    singleLine: Boolean = true,
    required: Boolean = false,
    initEmptyError: Boolean = false, // Show error even if the TextField has not been touched
    isLastField: Boolean = false,
    onGloballyPositioned: (LayoutCoordinates) -> Unit = {}
) {
    var isFirst by rememberSaveable { mutableStateOf(true) }
    val errorMessage =
        if (required && (initEmptyError || !isFirst) && value.isEmpty())
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