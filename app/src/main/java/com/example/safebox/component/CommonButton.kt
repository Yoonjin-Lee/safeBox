package com.example.safebox.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.safebox.R

enum class ButtonStyle {
    Dark,
    Light
}

@Composable
fun CommonButton(
    buttonStyle: ButtonStyle,
    text: String,
    onClick: () -> Unit,
    isEnabled: Boolean
){
    val backgroundColor = when(buttonStyle){
        ButtonStyle.Dark -> MaterialTheme.colorScheme.surfaceVariant
        ButtonStyle.Light -> MaterialTheme.colorScheme.primary
    }
    val textColor = when (buttonStyle) {
        ButtonStyle.Dark -> MaterialTheme.colorScheme.onSurface
        ButtonStyle.Light -> MaterialTheme.colorScheme.onPrimary
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .background(backgroundColor, RoundedCornerShape(1000.dp))
            .clickable(enabled = isEnabled) { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(color = textColor)
        )
    }
}