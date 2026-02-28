package com.armagan.cinevo.ui.customcomposables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomDivider(paddingval:PaddingValues){
    HorizontalDivider(
        color = MaterialTheme.colorScheme.secondary,
        thickness = 3.dp,
        modifier = Modifier
            .fillMaxWidth().padding(paddingval)
    )
}