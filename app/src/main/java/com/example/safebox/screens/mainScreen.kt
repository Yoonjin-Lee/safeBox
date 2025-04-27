package com.example.safebox.screens

import android.content.Context
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.safebox.R
import com.example.safebox.viewModel.MainViewModel
import dagger.hilt.android.qualifiers.ApplicationContext

@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    @ApplicationContext context: Context
) {
    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // 사용자가 선택한 사진의 uri
            uri?.let {
                val originalBitmap = context.contentResolver.openInputStream(uri)?.use {
                    BitmapFactory.decodeStream(it)
                } ?: throw IllegalArgumentException("Invalid Uri")
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
    }
}