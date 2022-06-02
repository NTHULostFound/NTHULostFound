package ss.team16.nthulostfound.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.NoPhotography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun ImageCarousel(
    images: List<Bitmap> = emptyList(),
    padding: PaddingValues = PaddingValues(0.dp),
    aspectRatio: Float = 4 / 3f,
    shape: Shape = RoundedCornerShape(16.dp),
    contextScale: ContentScale = ContentScale.FillWidth,
    borderWidth: Dp = 4.dp,
    borderColor: Color = Color.LightGray,
    addImage: Boolean = false,
    onAddImage: (Uri?, Context) -> Unit = { _, _ -> },
    deleteButton: Boolean = false,
    onDeleteImage: (Int) -> Unit = {},
) {
    val pagerState = rememberPagerState()

    Box(
        Modifier
            .fillMaxSize()
            .padding(padding)
            .aspectRatio(aspectRatio)
            .clip(shape)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = shape
            )
    ) {
        HorizontalPager(
            count = if (addImage) images.size + 1 else images.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            if (page == images.size) { // Add image
                val context = LocalContext.current

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent(),
                    onResult = { uri -> onAddImage(uri, context) }
                )

                Surface(
                    color = MaterialTheme.colors.secondaryVariant,
                    modifier = Modifier.fillMaxSize(),
                    onClick = {
                        launcher.launch("image/*")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "新增照片",
                        modifier = Modifier
                            .scale(0.4f)
                    )
                }
            } else {
                Image(
                    bitmap = images[page].asImageBitmap(),
                    contentDescription = null,
                    contentScale = contextScale,
                    modifier = Modifier.fillMaxWidth()
                )
                if (deleteButton)
                    IconButton(
                        onClick = { onDeleteImage(page) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "刪除照片",
                            modifier = Modifier.scale(1.5f)
                        )
                    }
            }
        }

        if (addImage || images.isNotEmpty()) {
            if (pagerState.pageCount > 1)
                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomCenter)
                )
        } else  { // No image
            Surface(
                color = MaterialTheme.colors.secondaryVariant,
                modifier = Modifier.fillMaxSize(),
            ) {
                Icon(
                    imageVector = Icons.Filled.NoPhotography,
                    contentDescription = "無照片",
                    modifier = Modifier
                        .scale(0.4f)
                )
            }
        }
    }
}