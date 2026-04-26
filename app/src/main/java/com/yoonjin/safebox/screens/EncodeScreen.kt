package com.yoonjin.safebox.screens

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yoonjin.safebox.R
import com.yoonjin.safebox.component.ButtonStyle
import com.yoonjin.safebox.component.CommonButton
import com.yoonjin.safebox.component.Header
import com.yoonjin.safebox.component.LoadingDialog
import kotlinx.coroutines.launch

@Composable
fun EncodeScreen(
    onBack: () -> Unit = {},
    bitmap: Bitmap? = null,
    onEncode: suspend (Bitmap, String, String) -> Unit = { _, _, _ -> }
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var title by remember { mutableStateOf("") }
    var key by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val isEnabled = title.isNotBlank() && key.isNotBlank()
    val encodeErrorMessage = stringResource(R.string.encode_error)

    if (isLoading) {
        LoadingDialog(
            title = stringResource(R.string.encode_loading_title),
            message = stringResource(R.string.encode_loading_message)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Header(
                    title = stringResource(R.string.encode_photo),
                    backIcon = R.drawable.round_arrow_back_24,
                    onBackClick = onBack
                )
                Spacer(Modifier.height(8.dp))

                // 이미지 프리뷰
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.FillWidth
                        )
                    }
                    // 3분할 오버레이
                    Row(
                        modifier = Modifier
                            .matchParentSize()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(3) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        MaterialTheme.colorScheme.background.copy(alpha = 0.25f)
                                    )
                            )
                        }
                    }
                    // 3분할 라벨
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.split_label),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                fontSize = 10.sp
                            )
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))

                // 제목 입력 섹션
                Text(
                    text = stringResource(R.string.title_section_label),
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Medium
                    )
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    BasicTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    if (title.isBlank()) {
                        Text(
                            text = stringResource(R.string.title_placeholder),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))

                // 키 입력 섹션
                Text(
                    text = stringResource(R.string.key_section_label),
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Medium
                    )
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    BasicTextField(
                        value = key,
                        onValueChange = { key = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    if (key.isBlank()) {
                        Text(
                            text = stringResource(R.string.input_key_placeholder),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))

                // 경고 텍스트
                Text(
                    text = stringResource(R.string.encode_warning),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                    )
                )
            }
        }
        item {
            CommonButton(
                buttonStyle = ButtonStyle.Light,
                text = stringResource(R.string.encode),
                onClick = {
                    bitmap?.let {
                        scope.launch {
                            isLoading = true
                            try {
                                onEncode(bitmap, title, key)
                                onBack()
                            } catch (e: Exception) {
                                Toast.makeText(context, encodeErrorMessage, Toast.LENGTH_SHORT)
                                    .show()
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                },
                isEnabled = isEnabled && !isLoading
            )
        }
    }
}
