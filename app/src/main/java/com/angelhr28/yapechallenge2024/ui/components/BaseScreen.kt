package com.angelhr28.yapechallenge2024.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.angelhr28.yapechallenge2024.ui.theme.YapeTheme

@Composable
fun BaseScreen(content: @Composable () -> Unit) {
    YapeTheme {
        val surfaceModifier = Modifier.fillMaxSize()
        Surface(modifier = surfaceModifier) {
            content()
        }
    }
}
