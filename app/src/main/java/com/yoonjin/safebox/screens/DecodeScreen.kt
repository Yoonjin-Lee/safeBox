package com.yoonjin.safebox.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.createBitmap
import com.yoonjin.safebox.R
import com.yoonjin.safebox.common.ImageSaver
import com.yoonjin.safebox.component.ButtonStyle
import com.yoonjin.safebox.component.CommonButton
import com.yoonjin.safebox.component.Header
import com.yoonjin.safebox.component.LoadingDialog
import com.yoonjin.safebox.viewModel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DecodeScreen(
    onBackClick: () -> Unit,
    groupName: String,
    name: String,
    decryptKey: String,
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val decodeTime = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault()).format(Date())
    val saveSuccessMessageFormat = stringResource(R.string.save_success, "%s")
    val saveFailureMessage = stringResource(R.string.save_failure)

    var isRevealed by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var decodeError by remember { mutableStateOf<DecodeError?>(null) }
    var mergedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val errorMessage = when (val error = decodeError) {
        is DecodeError.InsufficientImageParts -> {
            stringResource(R.string.decode_error_insufficient_image_parts, error.partsCount)
        }
        DecodeError.Failure -> stringResource(R.string.decode_failure_check_key)
        null -> null
    }

    if (isLoading) {
        LoadingDialog(
            title = stringResource(R.string.decode_loading_title),
            message = stringResource(R.string.decode_loading_message)
        )
    }

    // 비동기로 복호화 후 비트맵 병합
    LaunchedEffect(groupName, decryptKey) {
        isLoading = true
        decodeError = null
        val result = withContext(Dispatchers.IO) {
            try {
                val decryptedParts = viewModel.getEncodedBitmaps(groupName, decryptKey)
                Log.d("SafeBoxLog", "decryptedParts: ${decryptedParts.size}")
                if (decryptedParts.size < 3) {
                    return@withContext Result.failure<Bitmap>(
                        InsufficientImagePartsException(decryptedParts.size)
                    )
                }
                val bitmaps = decryptedParts.map { bytes ->
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        ?: throw BitmapDecodeException()
                }
                Result.success(mergeBitmapsHorizontal(bitmaps[0], bitmaps[1], bitmaps[2]))
            } catch (e: Exception) {
                Log.e("SafeBoxLog", e.toString())
                Result.failure(e)
            }
        }
        mergedBitmap = result.getOrNull()
        val failure = result.exceptionOrNull()
        decodeError = when {
            failure == null -> null
            failure is InsufficientImagePartsException -> {
                DecodeError.InsufficientImageParts(failure.partsCount)
            }
            else -> DecodeError.Failure
        }
        if (mergedBitmap != null) {
            decodeError = null
        }
        isLoading = false
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Header(
                    title = stringResource(R.string.decode_screen),
                    onBackClick = onBackClick,
                    backIcon = R.drawable.round_arrow_back_24
                )
                Spacer(Modifier.height(8.dp))

                when {
                    // 로딩 중
                    isLoading -> {
                        Spacer(Modifier.height(240.dp))
                    }

                    // 에러 발생
                    errorMessage != null -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.errorContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "⚠",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                )
                                Text(
                                    text = errorMessage,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                )
                            }
                        }
                    }

                    // 복호화 성공
                    mergedBitmap != null -> {
                        // 성공 인디케이터
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                            Text(
                                text = stringResource(R.string.decode_success),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                        Spacer(Modifier.height(12.dp))

                        // 복호화된 이미지 (블러 토글)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { isRevealed = !isRevealed }
                        ) {
                            Image(
                                bitmap = mergedBitmap!!.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .then(if (!isRevealed) Modifier.blur(20.dp) else Modifier),
                                contentScale = ContentScale.FillWidth
                            )

                            // 블러 상태일 때 오버레이
                            if (!isRevealed) {
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .background(
                                            MaterialTheme.colorScheme.background.copy(alpha = 0.25f)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.round_arrow_back_24),
                                            contentDescription = null,
                                            modifier = Modifier.size(28.dp),
                                            tint = MaterialTheme.colorScheme.onBackground.copy(
                                                alpha = 0.7f
                                            )
                                        )
                                        Text(
                                            text = stringResource(R.string.tap_to_reveal),
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                color = MaterialTheme.colorScheme.onBackground.copy(
                                                    alpha = 0.7f
                                                ),
                                                fontWeight = FontWeight.Medium
                                            )
                                        )
                                    }
                                }
                            }

                            // 공개 상태일 때 힌트 라벨
                            if (isRevealed) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(10.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(
                                            MaterialTheme.colorScheme.background.copy(alpha = 0.7f)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.tap_to_hide),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = MaterialTheme.colorScheme.onBackground.copy(
                                                alpha = 0.7f
                                            ),
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(16.dp))

                        // 파일 정보 카드
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                )
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            InfoRow(
                                label = stringResource(R.string.decode_time),
                                value = decodeTime
                            )
                            InfoRow(
                                label = stringResource(R.string.decode_file_name),
                                value = name
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }

        // 저장/닫기 버튼 — 복호화 성공 시에만 표시
        if (!isLoading && errorMessage == null && mergedBitmap != null) {
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    CommonButton(
                        buttonStyle = ButtonStyle.Light,
                        text = stringResource(R.string.save),
                        onClick = {
                            scope.launch(Dispatchers.IO) {
                                val saved = ImageSaver.saveBitmapToGallery(
                                    context = context,
                                    bitmap = mergedBitmap!!,
                                    baseFileName = "safebox_${System.currentTimeMillis()}",
                                    format = ImageSaver.ImageFormat.JPEG,
                                    quality = 92
                                )
                                withContext(Dispatchers.Main) {
                                    val msg = if (saved != null) {
                                        saveSuccessMessageFormat.format(saved)
                                    } else {
                                        saveFailureMessage
                                    }
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        isEnabled = true
                    )
                    CommonButton(
                        buttonStyle = ButtonStyle.Dark,
                        text = stringResource(R.string.close),
                        onClick = onBackClick,
                        isEnabled = true
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        } else if (!isLoading && errorMessage != null) {
            // 에러 시 닫기 버튼만
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    CommonButton(
                        buttonStyle = ButtonStyle.Dark,
                        text = stringResource(R.string.close),
                        onClick = onBackClick,
                        isEnabled = true
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f),
                fontSize = 11.sp
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

private sealed class DecodeError {
    data class InsufficientImageParts(val partsCount: Int) : DecodeError()
    data object Failure : DecodeError()
}

private class InsufficientImagePartsException(
    val partsCount: Int
) : IllegalStateException()

private class BitmapDecodeException : IllegalStateException()

fun mergeBitmapsHorizontal(b1: Bitmap, b2: Bitmap, b3: Bitmap): Bitmap {
    val width = b1.width + b2.width + b3.width
    val height = maxOf(b1.height, b2.height, b3.height)
    val result = createBitmap(width, height)
    val canvas = Canvas(result)
    var x = 0
    canvas.drawBitmap(b1, x.toFloat(), 0f, null); x += b1.width
    canvas.drawBitmap(b2, x.toFloat(), 0f, null); x += b2.width
    canvas.drawBitmap(b3, x.toFloat(), 0f, null)
    return result
}
