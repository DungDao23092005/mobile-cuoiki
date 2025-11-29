package com.stushare.feature_contribution.ui.account

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stushare.feature_contribution.R
import com.stushare.feature_contribution.ui.theme.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onAccountSecurityClick: () -> Unit,
    onNotificationSettingsClick: () -> Unit,
    onAppearanceSettingsClick: () -> Unit,
    onAboutAppClick: () -> Unit,
    onContactSupportClick: () -> Unit,
    onReportViolationClick: () -> Unit,
    onSwitchAccountClick: () -> Unit
) {
    val context = LocalContext.current
    val backgroundColor = MaterialTheme.colorScheme.background

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings_title),
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
        containerColor = backgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(top = 12.dp)
        ) {
            // 1. Tài khoản
            SettingsItem(
                icon = Icons.Default.Person,
                title = stringResource(R.string.acc_security),
                onClick = onAccountSecurityClick
            )
            
            Spacer(modifier = Modifier.height(2.dp))

            // 2. Thông báo
            SettingsItem(
                icon = Icons.Default.Notifications,
                title = stringResource(R.string.notifications),
                onClick = onNotificationSettingsClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 3. Giao diện
            SettingsItem(
                icon = Icons.Default.Face,
                title = stringResource(R.string.appearance_language),
                onClick = onAppearanceSettingsClick
            )

            Spacer(modifier = Modifier.height(2.dp))

            // 4. Thông tin
            SettingsItem(
                icon = Icons.Default.Info,
                title = stringResource(R.string.about_stushare),
                onClick = onAboutAppClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 5. Hỗ trợ
            SettingsItem(
                icon = Icons.Default.Call,
                title = stringResource(R.string.contact_support),
                onClick = onContactSupportClick
            )

            Spacer(modifier = Modifier.height(2.dp))

            // 6. Báo cáo
            SettingsItem(
                icon = Icons.Default.Warning,
                title = stringResource(R.string.report_violation),
                onClick = onReportViolationClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 7. Chuyển tài khoản
            SettingsItem(
                icon = Icons.Default.AccountBox, 
                title = stringResource(R.string.switch_account),
                onClick = onSwitchAccountClick
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Nút Đăng xuất
            val logoutMsg = stringResource(R.string.logout_success)
            Button(
                onClick = { Toast.makeText(context, logoutMsg, Toast.LENGTH_SHORT).show() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B6B)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(220.dp)
                    .height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.logout),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(surfaceColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = GreenPrimary,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = onSurfaceColor,
            modifier = Modifier.weight(1f)
        )
        
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = onSurfaceColor.copy(alpha = 0.5f),
            modifier = Modifier.size(24.dp)
        )
    }
}