package com.example.safebox.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.safebox.R

enum class ButtonStyle{
    Dark,
    Light
}

@Composable
fun CommonButton(
    buttonStyle: ButtonStyle,
    text: String,
    onClick: () -> Unit
){
    val backgroundColor = when(buttonStyle){
        ButtonStyle.Dark -> colorResource(R.color.dark_button_color)
        ButtonStyle.Light -> colorResource(R.color.light_button_color)
    }
    Row(
        modifier = Modifier.padding(20.dp)
            .background(backgroundColor, RoundedCornerShape(1000.dp))
            .clickable{
                onClick()
            }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(Color.White)
        )
    }
}