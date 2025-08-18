package com.example.safebox.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.core.graphics.createBitmap
import com.example.safebox.R
import com.example.safebox.common.ImageSaver
import com.example.safebox.component.ButtonStyle
import com.example.safebox.component.CommonButton
import com.example.safebox.component.Header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DecodeScreen(
    onBackClick: () -> Unit,
    decodedByteArrays: List<ByteArray>,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
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
            onClick = {
                scope.launch(Dispatchers.IO) {
                    val decodedBitmaps = decodedByteArrays.map {
                        BitmapFactory.decodeByteArray(it, 0, it.size)
                    }

                    val newBitmap = mergeBitmapsHorizontal(
                        decodedBitmaps[0],
                        decodedBitmaps[1],
                        decodedBitmaps[2]
                    )

                    val saved = ImageSaver.saveBitmapToGallery(
                        context = context,
                        bitmap = newBitmap,
                        baseFileName = "profile_${System.currentTimeMillis()}",
                        format = ImageSaver.ImageFormat.JPEG, // PNG/WEBP 가능
                        quality = 92
                    )
                    withContext(Dispatchers.Main) {
                        if (saved != null) {
                            Toast.makeText(context, "저장됨: $saved", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "저장 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            isEnabled = true
        )
    }
}

fun mergeBitmapsHorizontal(b1: Bitmap, b2: Bitmap, b3: Bitmap): Bitmap {
    val width = b1.width + b2.width + b3.width
    val height = maxOf(b1.height, b2.height, b3.height)

    val result = createBitmap(width, height)
    val canvas = Canvas(result)

    var x = 0
    canvas.drawBitmap(b1, x.toFloat(), 0f, null)
    x += b1.width
    canvas.drawBitmap(b2, x.toFloat(), 0f, null)
    x += b2.width
    canvas.drawBitmap(b3, x.toFloat(), 0f, null)

    return result
}