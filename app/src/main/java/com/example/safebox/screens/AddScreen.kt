package com.example.safebox.screens

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.safebox.R
import com.example.safebox.component.ButtonStyle
import com.example.safebox.component.CommonButton
import com.example.safebox.component.Header
import com.example.safebox.viewModel.MainViewModel

@Preview
@Composable
fun AddScreen(
    onBack: () -> Unit = {}
){
    val mainViewModel: MainViewModel = hiltViewModel()
    val context = LocalContext.current
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
            .background(colorResource(R.color.background_green))
    ) {
        Header(
            title = stringResource(R.string.upload_photos),
            backIcon = R.drawable.round_arrow_back_24,
            onBackClick = onBack
        )
        Text(
            modifier = Modifier.padding(20.dp),
            text = stringResource(R.string.upload_photos),
            style = MaterialTheme.typography.labelLarge.copy(Color.White),
        )
        Text(
            stringResource(R.string.upload_photos_info),
            Modifier.padding(horizontal = 20.dp),
            style = MaterialTheme.typography.titleSmall.copy(Color.White),
        )
        CommonButton(
            buttonStyle = ButtonStyle.Dark,
            text = stringResource(R.string.select_photo),
            onClick = {
                pickMedia.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        )
    }
}