package com.stushare.feature_contribution.ui.account

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stushare.feature_contribution.ui.theme.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactSupportScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Liên hệ hỗ trợ",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GreenPrimary)
            )
        },
        containerColor = Color(0xFFF0F0F0)
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // --- Section: Trực tuyến ---
                SupportSectionHeader(title = "Kênh hỗ trợ trực tuyến")

                // Item 1: Chat
                SupportItem(
                    icon = Icons.Default.Face,
                    title = "Chat với nhân viên hỗ trợ",
                    subtitle = "Phản hồi trung bình trong 5 phút",
                    onClick = { /* TODO: Open Chat */ }
                )

                HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))

                // Item 2: Email
                SupportItem(
                    icon = Icons.Default.Email,
                    title = "Gửi email yêu cầu",
                    subtitle = "support@stushare.com",
                    onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:support@stushare.com")
                            putExtra(Intent.EXTRA_SUBJECT, "Hỗ trợ StuShare")
                        }
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // Handle error
                        }
                    }
                )

                // --- Section: Kênh khác ---
                SupportSectionHeader(title = "Kênh khác")

                // Item 3: Hotline
                SupportItem(
                    icon = Icons.Default.Call,
                    title = "Hotline",
                    subtitle = "1900 1234 (8:00 - 17:00)",
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:19001234")
                        }
                        context.startActivity(intent)
                    }
                )

                HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))

                // Item 4: FAQ
                SupportItem(
                    icon = Icons.Default.Info,
                    title = "Câu hỏi thường gặp (FAQ)",
                    subtitle = "Xem các vấn đề phổ biến",
                    onClick = { /* TODO: Open Web FAQ */ }
                )
            }

            // --- Bottom Curve ---
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(120.dp)
                    .offset(y = 60.dp)
                    .background(
                        color = GreenPrimary,
                        shape = RoundedCornerShape(topStart = 1000.dp, topEnd = 1000.dp)
                    )
            )
        }
    }
}

@Composable
fun SupportSectionHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE0E0E0))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = title,
            color = GreenPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}

@Composable
fun SupportItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = GreenPrimary,
            modifier = Modifier.size(28.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
        
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}