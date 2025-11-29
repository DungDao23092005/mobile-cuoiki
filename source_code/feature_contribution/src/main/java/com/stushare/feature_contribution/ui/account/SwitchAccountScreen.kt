package com.stushare.feature_contribution.ui.account

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource // Import quan trọng
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stushare.feature_contribution.R
import com.stushare.feature_contribution.ui.theme.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwitchAccountScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    // Giả lập ID tài khoản đang active
    var activeAccountId by remember { mutableStateOf("user1") }

    // Dữ liệu giả lập
    val accounts = listOf(
        AccountInfo("user1", "Dũng Đào", "dungdao@test.com"),
        AccountInfo("user2", "Nguyễn Văn A", "nguyenvana@gmail.com"),
        AccountInfo("user3", "Trần Thị B", "tranthib@school.edu.vn")
    )

    // Lấy màu động từ Theme
    val backgroundColor = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val dividerColor = MaterialTheme.colorScheme.outlineVariant

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.switch_acc_header),
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
                    .padding(top = 12.dp)
            ) {
                // Danh sách tài khoản
                accounts.forEach { account ->
                    AccountItemRow(
                        account = account,
                        isActive = account.id == activeAccountId,
                        onClick = {
                            activeAccountId = account.id
                            val msg = context.getString(R.string.switch_acc_switched, account.name)
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                    )
                    HorizontalDivider(color = dividerColor)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Nút thêm tài khoản
                Button(
                    onClick = { Toast.makeText(context, "Chức năng Thêm tài khoản", Toast.LENGTH_SHORT).show() },
                    colors = ButtonDefaults.buttonColors(containerColor = surfaceColor), // Nền nút động
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = GreenPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.switch_acc_add),
                        color = GreenPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Bottom Curve
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

data class AccountInfo(val id: String, val name: String, val email: String)

@Composable
fun AccountItemRow(
    account: AccountInfo,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    // Màu nền khi active: Xanh nhạt (Sáng) hoặc Xám đậm (Tối)
    val activeBgColor = if (isActive) GreenPrimary.copy(alpha = 0.1f) else surfaceColor

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(activeBgColor) 
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = if (isActive) GreenPrimary else MaterialTheme.colorScheme.surfaceVariant
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = if (isActive) Color.White else onSurfaceColor.copy(alpha = 0.5f),
                modifier = Modifier.padding(10.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = account.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = onSurfaceColor // Chữ động
            )
            Text(
                text = account.email,
                fontSize = 14.sp,
                color = onSurfaceColor.copy(alpha = 0.6f)
            )
        }

        // Check icon (chỉ hiện khi active)
        if (isActive) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(R.string.switch_acc_active),
                tint = GreenPrimary
            )
        }
    }
}