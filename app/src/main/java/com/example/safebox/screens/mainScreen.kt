package com.example.safebox.screens

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.safebox.R
import com.example.safebox.component.Header
import com.example.safebox.viewModel.MainViewModel

@Preview
@Composable
fun MainScreen(
) {
    val mainViewModel: MainViewModel = hiltViewModel()
    val context = LocalContext.current
    val imageEntityList by mainViewModel.bitmaps.collectAsStateWithLifecycle()

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // 사용자가 선택한 사진의 uri
            uri?.let {
                val originalBitmap = context.contentResolver.openInputStream(uri)?.use {
                    BitmapFactory.decodeStream(it)
                } ?: throw IllegalArgumentException("Invalid Uri")
                val bitmapList = mainViewModel.splitBitmap(originalBitmap)
                val encodedList = mainViewModel.encodeBitmap(bitmapList)
                mainViewModel.saveBitmapParts(encodedList)
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(10.dp)
    ) {
        Header(
            title = stringResource(R.string.photos),
            menuIcon = R.drawable.round_add_24,
            onMenuClick = {
                pickMedia.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        )
        LazyVerticalGrid(
            modifier = Modifier.weight(1f),
            columns = GridCells.Fixed(2)
        ) {
            itemsIndexed(imageEntityList){ index, item ->
                val bitmap = BitmapFactory.decodeByteArray(item.byteArray, 0, item.byteArray.size)

                bitmap?.let {
                    Image(
                        painter = BitmapPainter(it.asImageBitmap()),
                        contentDescription = null,
                        modifier = Modifier.size(200.dp)
                    )
                } ?: Text("이미지를 불러올 수 없습니다")
            }
        }
    }
}

