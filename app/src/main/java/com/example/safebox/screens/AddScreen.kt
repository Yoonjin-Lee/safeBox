package com.example.safebox.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.safebox.R
import com.example.safebox.component.Header
import com.example.safebox.navigation.Screens.AddScreen

@Preview
@Composable
fun AddScreen(
    onBack: () -> Unit = {}
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_green))
    ) {
        Header(
            title = stringResource(R.string.upload_photos),
            backIcon = R.drawable.round_arrow_back_24,
            onBackClick = onBack
        )
        Text(
            modifier = Modifier.padding(20.dp),
            text = stringResource(R.string.upload_photos),
            style = MaterialTheme.typography.labelLarge.copy(Color.White),
        )
        Text(
            stringResource(R.string.upload_photos_info),
            Modifier.padding(20.dp),
            style = MaterialTheme.typography.titleMedium.copy(Color.White),
        )
    }
}