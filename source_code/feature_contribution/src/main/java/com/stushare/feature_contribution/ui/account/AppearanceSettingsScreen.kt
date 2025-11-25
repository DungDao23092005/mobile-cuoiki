package com.stushare.feature_contribution.ui.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stushare.feature_contribution.ui.theme.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceSettingsScreen(
    onBackClick: () -> Unit,
    viewModel: AppearanceViewModel = viewModel()
) {
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
    val currentLang by viewModel.language.collectAsStateWithLifecycle()
    val currentFontScale by viewModel.fontScale.collectAsStateWithLifecycle()

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showFontDialog by remember { mutableStateOf(false) }

    // Màu nền động: Sáng -> F0F0F0, Tối -> 121212 (Lấy từ Theme)
    val backgroundColor = MaterialTheme.colorScheme.background 
    // Màu thẻ động: Sáng -> White, Tối -> 1E1E1E
    val surfaceColor = MaterialTheme.colorScheme.surface
    // Màu chữ động: Sáng -> Black, Tối -> White
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Giao diện & ngôn ngữ", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GreenPrimary)
            )
        },
        containerColor = backgroundColor // <--- Dùng màu động
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                Spacer(modifier = Modifier.height(12.dp))

                // 1. Giao diện Sáng/Tối
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(surfaceColor) // <--- Dùng màu động
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Settings, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Giao diện Tối (Dark Mode)", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = onSurfaceColor, modifier = Modifier.weight(1f))
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { viewModel.toggleTheme(it) },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = GreenPrimary,
                            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                // 2. Đổi cỡ chữ
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(surfaceColor) // <--- Dùng màu động
                        .clickable { showFontDialog = true }
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Đổi cỡ chữ", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = onSurfaceColor, modifier = Modifier.weight(1f))
                    Text(
                        text = when(currentFontScale) {
                            0.85f -> "Nhỏ"
                            1.15f -> "Lớn"
                            else -> "Vừa"
                        },
                        color = onSurfaceColor.copy(alpha = 0.7f), fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = onSurfaceColor.copy(alpha = 0.5f))
                }

                // 3. Ngôn ngữ header
                Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant).padding(16.dp, 8.dp)) {
                    Text("Ngôn ngữ", color = GreenPrimary, fontWeight = FontWeight.Bold)
                }

                // Item Ngôn ngữ
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(surfaceColor) // <--- Dùng màu động
                        .clickable { showLanguageDialog = true }
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Thay đổi ngôn ngữ", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = onSurfaceColor, modifier = Modifier.weight(1f))
                    Icon(Icons.Default.Info, contentDescription = null, tint = onSurfaceColor.copy(alpha = 0.5f), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (currentLang == "vi") "Tiếng Việt" else "English", fontSize = 15.sp, color = onSurfaceColor)
                }
            }
            
            // Bottom Curve
            Box(
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(120.dp).offset(y = 60.dp)
                    .background(GreenPrimary, RoundedCornerShape(topStart = 1000.dp, topEnd = 1000.dp))
            )
        }
    }

    // Dialogs (Sử dụng màu mặc định của MaterialTheme nên sẽ tự dark mode)
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text("Chọn ngôn ngữ") },
            text = {
                Column {
                    LanguageOption("Tiếng Việt", selected = currentLang == "vi") { viewModel.setLanguage("vi"); showLanguageDialog = false }
                    LanguageOption("English", selected = currentLang == "en") { viewModel.setLanguage("en"); showLanguageDialog = false }
                }
            },
            confirmButton = { TextButton(onClick = { showLanguageDialog = false }) { Text("Đóng") } }
        )
    }

    if (showFontDialog) {
        AlertDialog(
            onDismissRequest = { showFontDialog = false },
            title = { Text("Chọn cỡ chữ") },
            text = {
                Column {
                    FontOption("Nhỏ", selected = currentFontScale == 0.85f) { viewModel.setFontScale(0.85f); showFontDialog = false }
                    FontOption("Vừa (Mặc định)", selected = currentFontScale == 1.0f) { viewModel.setFontScale(1.0f); showFontDialog = false }
                    FontOption("Lớn", selected = currentFontScale == 1.15f) { viewModel.setFontScale(1.15f); showFontDialog = false }
                }
            },
            confirmButton = { TextButton(onClick = { showFontDialog = false }) { Text("Đóng") } }
        )
    }
}

@Composable
fun LanguageOption(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick, colors = RadioButtonDefaults.colors(selectedColor = GreenPrimary))
        Text(text, modifier = Modifier.padding(start = 8.dp), color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun FontOption(text: String, selected: Boolean, onClick: () -> Unit) {
    LanguageOption(text, selected, onClick)
}