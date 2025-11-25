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
import androidx.compose.ui.res.stringResource // Import stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stushare.feature_contribution.R
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
    // Màu nền động
    val backgroundColor = MaterialTheme.colorScheme.background
    val dividerColor = MaterialTheme.colorScheme.outlineVariant

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.acc_sec_header),
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
        containerColor = backgroundColor // Áp dụng màu nền động
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Tài khoản
                SectionHeader(title = stringResource(R.string.acc_sec_account_group))

                AccountItem(
                    title = stringResource(R.string.acc_sec_personal_info),
                    subtitle = stringResource(R.string.acc_sec_username_label),
                    iconVector = Icons.Default.Person,
                    isAvatar = true,
                    onClick = onPersonalInfoClick
                )
                
                HorizontalDivider(thickness = 1.dp, color = dividerColor)

                AccountItem(
                    title = stringResource(R.string.acc_sec_phone),
                    subtitle = "(+84) 123 456 789",
                    iconVector = Icons.Default.Phone,
                    onClick = onPhoneClick
                )

                HorizontalDivider(thickness = 1.dp, color = dividerColor)

                AccountItem(
                    title = stringResource(R.string.acc_sec_email),
                    subtitle = stringResource(R.string.acc_sec_not_linked),
                    iconVector = Icons.Default.Email,
                    onClick = onEmailClick
                )

                // Bảo mật
                SectionHeader(title = stringResource(R.string.acc_sec_security_group))

                AccountItem(
                    title = stringResource(R.string.acc_sec_password),
                    subtitle = null,
                    iconVector = Icons.Default.Lock,
                    onClick = onPasswordClick
                )

                // Vô hiệu hóa
                SectionHeader(title = stringResource(R.string.acc_sec_disable_group))

                AccountItem(
                    title = stringResource(R.string.acc_sec_delete_acc),
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
    // Màu thẻ động
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(surfaceColor) // Nền thẻ động
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (iconVector != null) {
            if (isAvatar) {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant // Nền avatar động
                ) {
                    Icon(
                        imageVector = iconVector,
                        contentDescription = null,
                        tint = onSurfaceColor.copy(alpha = 0.6f),
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
                color = onSurfaceColor // Chữ động
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    fontWeight = if (isAvatar) FontWeight.Bold else FontWeight.Normal,
                    color = if (isAvatar) onSurfaceColor else onSurfaceColor.copy(alpha = 0.6f)
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = onSurfaceColor.copy(alpha = 0.4f)
        )
    }
}