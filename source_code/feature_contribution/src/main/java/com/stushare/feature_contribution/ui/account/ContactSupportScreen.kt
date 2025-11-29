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
import androidx.compose.ui.res.stringResource // Import quan trọng
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stushare.feature_contribution.R
import com.stushare.feature_contribution.ui.theme.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactSupportScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    
    // Lấy màu động từ Theme
    val backgroundColor = MaterialTheme.colorScheme.background
    val dividerColor = MaterialTheme.colorScheme.outlineVariant

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.support_header),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GreenPrimary)
            )
        },
        containerColor = backgroundColor // Nền động
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // --- Section: Trực tuyến ---
                SupportSectionHeader(title = stringResource(R.string.support_online_channel))

                // Item 1: Chat
                SupportItem(
                    icon = Icons.Default.Face,
                    title = stringResource(R.string.support_chat_title),
                    subtitle = stringResource(R.string.support_chat_desc),
                    onClick = { /* TODO: Open Chat */ }
                )

                HorizontalDivider(thickness = 1.dp, color = dividerColor)

                // Item 2: Email
                SupportItem(
                    icon = Icons.Default.Email,
                    title = stringResource(R.string.support_email_title),
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
                SupportSectionHeader(title = stringResource(R.string.support_other_channel))

                // Item 3: Hotline
                SupportItem(
                    icon = Icons.Default.Call,
                    title = stringResource(R.string.support_hotline_title),
                    subtitle = stringResource(R.string.support_hotline_desc),
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:19001234")
                        }
                        context.startActivity(intent)
                    }
                )

                HorizontalDivider(thickness = 1.dp, color = dividerColor)

                // Item 4: FAQ
                SupportItem(
                    icon = Icons.Default.Info,
                    title = stringResource(R.string.support_faq_title),
                    subtitle = stringResource(R.string.support_faq_desc),
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
            // Nền header section động
            .background(MaterialTheme.colorScheme.surfaceVariant)
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
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(surfaceColor) // Nền item động
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
                color = onSurfaceColor // Chữ tiêu đề động
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = onSurfaceColor.copy(alpha = 0.6f) // Chữ phụ động (nhạt hơn)
            )
        }
        
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = onSurfaceColor.copy(alpha = 0.4f) // Icon mũi tên động
        )
    }
}