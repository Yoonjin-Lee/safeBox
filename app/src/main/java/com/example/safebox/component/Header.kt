package com.example.safebox.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.safebox.R

@Composable
fun Header(
    modifier: Modifier = Modifier,
    title: String,
    backIcon: Int? = null,
    onBackClick: () -> Unit = {},
    menuIcon: Int? = null,
    onMenuClick: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp) // 아이콘 버튼 크기
                .clickable(enabled = backIcon != null) { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            backIcon?.let {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .size(48.dp)
                .clickable(enabled = menuIcon != null) { onMenuClick() },
            contentAlignment = Alignment.Center
        ) {
            menuIcon?.let {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
fun HeaderPreview() {
    Header(
        title = "Title",
        backIcon = R.drawable.round_arrow_back_24,
        menuIcon = R.drawable.round_add_24
    )
}