package com.example.salecar.presentation_layer.screens.common_component

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex

@Composable
fun CustomLoadingBar(
    size: Dp = 40.dp,
    color: Color = Color.Transparent
) {
    Box(
        modifier = Modifier
            .zIndex(1f)
            .fillMaxSize().background(color), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(size),
            color = Color(0xfff68B8B)
        )
    }

}
