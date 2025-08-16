package com.example.safebox.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.safebox.R
import com.example.safebox.component.ButtonStyle
import com.example.safebox.component.CommonButton
import com.example.safebox.component.Header

@Composable
fun EncodeScreen(
    onBack: () -> Unit = {},
    bitmap: Bitmap? = null,
    onEncode: (Bitmap, String) -> Unit = {_, _ ->}
){
    var text by remember { mutableStateOf("") }
    LazyColumn (
        modifier = Modifier.fillMaxSize()
            .background(colorResource(R.color.background_green)),
    ) {
       item {
           Column (
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(16.dp)
           ){
                Header(
                    title = stringResource(R.string.encode_photo),
                    backIcon = R.drawable.round_arrow_back_24,
                    onBackClick = onBack
                )
                Image(
                    bitmap = bitmap?.asImageBitmap() ?: return@Column,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(R.color.dark_button_color), RoundedCornerShape(12.dp))
                        .padding(20.dp)
                ) {
                    BasicTextField(
                        value = text,
                        onValueChange = {
                            text = it
                        }
                    )
                    if (text.isBlank()) {
                        Text(
                            text = stringResource(R.string.input_key_placeholder),
                            modifier = Modifier.fillMaxWidth(),
                            color = colorResource(R.color.placeholder_color)
                        )
                    }
                }
            }
        }
        item{
            CommonButton(
                buttonStyle = ButtonStyle.Light,
                text = stringResource(R.string.encode),
                onClick = {
                    bitmap?.let {
                        onEncode(bitmap, text)
                        onBack()
                    }
                },
                isEnabled = text.isNotBlank()
            )
        }
    }
}