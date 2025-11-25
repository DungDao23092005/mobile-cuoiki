package com.stushare.feature_contribution.ui.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stushare.feature_contribution.ui.theme.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSecurityScreen(
    onBackClick: () -> Unit,
    onPersonalInfoClick: () -> Unit,
    onPhoneClick: () -> Unit,
    onEmailClick: () -> Unit,
    onPasswordClick: () -> Unit,
    onDeleteAccountClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tài khoản & bảo mật",
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
                    .verticalScroll(rememberScrollState())
            ) {
                SectionHeader(title = "Tài khoản")

                AccountItem(
                    title = "Thông tin cá nhân",
                    subtitle = "Tên người dùng",
                    iconVector = Icons.Default.Person,
                    isAvatar = true,
                    onClick = onPersonalInfoClick
                )
                
                HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))

                AccountItem(
                    title = "Số điện thoại",
                    subtitle = "(+84) 123 456 789",
                    iconVector = Icons.Default.Phone,
                    onClick = onPhoneClick
                )

                HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))

                AccountItem(
                    title = "Email",
                    subtitle = "Chưa liên kết",
                    iconVector = Icons.Default.Email,
                    onClick = onEmailClick
                )

                SectionHeader(title = "Bảo mật")

                AccountItem(
                    title = "Mật khẩu",
                    subtitle = null,
                    iconVector = Icons.Default.Lock,
                    onClick = onPasswordClick
                )

                SectionHeader(title = "Vô hiệu hóa")

                AccountItem(
                    title = "Xóa tài khoản",
                    subtitle = null,
                    iconVector = null,
                    onClick = onDeleteAccountClick
                )
                
                Spacer(modifier = Modifier.height(100.dp))
            }

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
fun SectionHeader(title: String) {
    Text(
        text = title,
        color = GreenPrimary,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
fun AccountItem(
    title: String,
    subtitle: String?,
    iconVector: ImageVector?,
    isAvatar: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (iconVector != null) {
            if (isAvatar) {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = Color(0xFFE0E0E0)
                ) {
                    Icon(
                        imageVector = iconVector,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            } else {
                Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    tint = GreenPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = if (isAvatar) FontWeight.Normal else FontWeight.Medium,
                color = Color.Black
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    fontWeight = if (isAvatar) FontWeight.Bold else FontWeight.Normal,
                    color = if (isAvatar) Color.Black else Color.Gray
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}