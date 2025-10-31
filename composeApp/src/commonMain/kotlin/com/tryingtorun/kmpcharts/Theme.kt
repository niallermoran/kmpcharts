package com.tryingtorun.kmpcharts

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) PulseIQColors.DarkColorScheme else PulseIQColors.LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        content = {
            content.invoke()
        },
    )
}
