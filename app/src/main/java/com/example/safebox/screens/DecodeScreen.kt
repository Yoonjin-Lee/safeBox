package com.example.safebox.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.example.safebox.R
import com.example.safebox.component.ButtonStyle
import com.example.safebox.component.CommonButton
import com.example.safebox.component.Header

@Composable
fun DecodeScreen(
    onBackClick: () -> Unit,
    decodedByteArrays: List<ByteArray>
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.background_green)),
    ) {
        Header(
            title = stringResource(R.string.decode_screen),
            onBackClick = onBackClick,
            backIcon = R.drawable.round_arrow_back_24
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            for (byteArray in decodedByteArrays) {
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null
                )
            }
        }
        CommonButton(
            text = stringResource(R.string.save),
            buttonStyle = ButtonStyle.Light,
            onClick = {},
            isEnabled = true
        )
    }
}