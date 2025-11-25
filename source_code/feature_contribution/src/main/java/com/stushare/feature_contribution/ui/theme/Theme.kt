package com.stushare.feature_contribution.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme // Thêm import này
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ... (Giữ nguyên các biến màu sắc) ...
val GreenPrimary = Color(0xFF27AE60)
val GreenVariant = Color(0xFF219150)
val OrangeWarning = Color(0xFFFF9800)
val BlueInfo = Color(0xFF2196F3)
val GrayBackground = Color(0xFFF0F0F0)

// Định nghĩa bảng màu Tối (Dark)
private val DarkColorScheme = darkColorScheme(
    primary = GreenPrimary,
    secondary = GreenVariant,
    background = Color(0xFF121212), // Màu nền tối
    surface = Color(0xFF1E1E1E),    // Màu thẻ tối
    onPrimary = Color.White,
    onSurface = Color.White
)

// Định nghĩa bảng màu Sáng (Light)
private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    secondary = GreenVariant,
    background = GrayBackground,
    surface = Color.White,
    onPrimary = Color.White,
    onSurface = Color.Black
)

@Composable
fun StuShareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Nhận tham số từ bên ngoài
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}