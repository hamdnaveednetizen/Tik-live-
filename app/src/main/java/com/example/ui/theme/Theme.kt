package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TikTokDarkColorScheme = darkColorScheme(
    primary = TikTokPink,
    secondary = TikTokCyan,
    tertiary = TikTokYellow,
    background = TikTokBlack,
    surface = TikTokDarkSurface,
    surfaceVariant = TikTokCard,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = TikTokTextWhite,
    onSurface = TikTokTextWhite,
    onSurfaceVariant = TikTokTextDim
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // TikTok is fundamentally an immersive dark media experience
    MaterialTheme(
        colorScheme = TikTokDarkColorScheme,
        typography = Typography,
        content = content
    )
}
