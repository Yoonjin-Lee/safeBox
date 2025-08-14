package com.example.safebox.screens

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.safebox.R
import com.example.safebox.component.Header
import com.example.safebox.viewModel.MainViewModel

@Preview
@Composable
fun MainScreen(
    goToAddScreen: () -> Unit = {},
) {

    val mainViewModel: MainViewModel = hiltViewModel()
    val context = LocalContext.current
    val imageEntityList by mainViewModel.bitmaps.collectAsStateWithLifecycle()
    var showInputKeyDialog by remember { mutableStateOf(false) }

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

    if (showInputKeyDialog){
        Dialog(onDismissRequest = { showInputKeyDialog = false }) {
            InputKeyDialog(
                onClick = {
                    goToAddScreen()
                    showInputKeyDialog = false
                },
                onDismiss = {
                    showInputKeyDialog = false
                }
            )
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
                showInputKeyDialog = true
//                pickMedia.launch(
//                    PickVisualMediaRequest(
//                        ActivityResultContracts.PickVisualMedia.ImageOnly
//                    )
//                )
            }
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (imageEntity in imageEntityList.groupBy { it.name }){
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = imageEntity.key ?: "key"
                        )
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(imageEntity.value) {
                                val imageBitmap = BitmapFactory.decodeByteArray(it.byteArray, 0, it.byteArray.size)
                                Image(
                                    bitmap = imageBitmap.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier.size(100.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun InputKeyDialog(
    onClick: (String) -> Unit = {},
    onDismiss: () -> Unit = {}
){
    var key = ""
    Column(
        modifier = Modifier
            .width(270.dp)
            .background(color = colorResource(R.color.light_gray), RoundedCornerShape(14.dp))
            .padding(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.input_key),
            style = MaterialTheme.typography.titleMedium
        )
        Box(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .background(color = colorResource(R.color.white), RoundedCornerShape(14.dp))
                .padding(10.dp)
        ){
            BasicTextField(
                value = key,
                onValueChange = { key = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            if(key.isEmpty()) {
                Text(
                    text = stringResource(R.string.input_key_placeholder),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        // todo 색 변경
                    )
                )
            }
        }
        HorizontalDivider()
        Text(
            modifier = Modifier.clickable{
                onClick(key)
            },
            text = stringResource(R.string.confirm),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
