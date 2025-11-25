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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Chuyển tài khoản",
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
                    .padding(top = 12.dp)
            ) {
                // Danh sách tài khoản
                accounts.forEach { account ->
                    AccountItemRow(
                        account = account,
                        isActive = account.id == activeAccountId,
                        onClick = {
                            activeAccountId = account.id
                            Toast.makeText(context, "Đã chuyển sang: ${account.name}", Toast.LENGTH_SHORT).show()
                            // Thực tế: Gọi ViewModel để đổi session và navigate về Home
                        }
                    )
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Nút thêm tài khoản
                Button(
                    onClick = { Toast.makeText(context, "Chức năng Thêm tài khoản", Toast.LENGTH_SHORT).show() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
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
                        text = "Thêm tài khoản mới",
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isActive) Color(0xFFE8F5E9) else Color.White) // Xanh nhạt nếu active
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = if (isActive) GreenPrimary else Color(0xFFE0E0E0)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.White,
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
                color = Color.Black
            )
            Text(
                text = account.email,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        // Check icon (chỉ hiện khi active)
        if (isActive) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Active",
                tint = GreenPrimary
            )
        }
    }
}