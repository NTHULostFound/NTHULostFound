package ss.team16.nthulostfound.ui.newitem

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ss.team16.nthulostfound.model.NewItemType
import ss.team16.nthulostfound.ui.components.FormTextField
import ss.team16.nthulostfound.ui.components.ImageCarousel
import kotlin.math.roundToInt

val padding = 24.dp

@Composable
fun EditPage(
    viewModel: NewItemViewModel
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var whoPosition  by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(padding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ImageCarousel(
            images = viewModel.imageBitmaps,
            padding = PaddingValues(bottom = 16.dp),
            addImage = true,
            onAddImage = { uri, context ->
                viewModel.onAddImage(uri, context)
            },
            deleteButton = true,
            onDeleteImage = { index ->
                viewModel.onDeleteImage(index)
            }
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

@Composable
fun WhoCheckBox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("您知道失主的資訊嗎？")
        Checkbox(
            checked = checked,
            onCheckedChange = { onCheckedChange(it) }
        )
    }
}
