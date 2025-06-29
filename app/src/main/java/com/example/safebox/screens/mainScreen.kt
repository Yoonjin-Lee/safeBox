package com.example.safebox.screens

import android.content.Context
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.safebox.R
import com.example.safebox.viewModel.MainViewModel
import dagger.hilt.android.qualifiers.ApplicationContext

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
                mainViewModel.saveBitmapParts(bitmapList)
            }
        }

    Column(
        modifier = Modifier.padding(10.dp)

    ) {
        Text(stringResource(R.string.upload_picture))
        Icon(
            painter = painterResource(R.drawable.round_add_24),
            contentDescription = null,
            modifier = Modifier.clickable {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        )
        HorizontalDivider()

        LazyColumn(){
            item {
                Text(stringResource(R.string.picture_list))
                Text(text = imageEntityList.size.toString(
                ))
            }
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