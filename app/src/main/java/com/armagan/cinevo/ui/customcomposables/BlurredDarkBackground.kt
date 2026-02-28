package com.armagan.cinevo.ui.customcomposables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.armagan.cinevo.R

@Composable
fun BlurredDarkBackground(){
    AsyncImage(
        model = R.drawable.mainpagebackgrounddark2,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .blur(5.dp)
    )
}