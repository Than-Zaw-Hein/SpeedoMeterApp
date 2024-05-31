package com.example.speedometerapp.ui.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun AppImage(modifier: Modifier = Modifier, @DrawableRes image: Int) {
    ElevatedCard() {
        Image(
            painter = painterResource(image),
            contentDescription = "Length Converter Image",
            contentScale = ContentScale.FillBounds,
            modifier = modifier
        )
    }
}