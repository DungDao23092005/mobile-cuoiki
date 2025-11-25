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
fun ReportViolationScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var selectedReason by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val reasons = listOf(
        "Nội dung rác / Spam",
        "Thông tin sai lệch",
        "Vi phạm bản quyền tài liệu",
        "Ngôn từ đả kích / Xúc phạm",
        "Lý do khác"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Báo cáo vi phạm",
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
                Spacer(modifier = Modifier.height(16.dp))

                // Title
                Text(
                    text = "Vấn đề bạn gặp phải là gì?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = GreenPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                // List Reasons (Danh sách lý do)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column {
                        reasons.forEachIndexed { index, reason ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedReason = reason }
                                    .padding(horizontal = 12.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (selectedReason == reason),
                                    onClick = { selectedReason = reason },
                                    colors = RadioButtonDefaults.colors(selectedColor = GreenPrimary)
                                )
                                Text(
                                    text = reason,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black
                                )
                            }
                            if (index < reasons.size - 1) {
                                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFEEEEEE))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Description (Mô tả)
                Text(
                    text = "Mô tả chi tiết (Tùy chọn)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = GreenPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Nhập thêm thông tin để chúng tôi hỗ trợ tốt hơn...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(150.dp)
                        .background(Color.White, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = Color.LightGray
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Submit Button (Nút gửi)
                Button(
                    onClick = {
                        if (selectedReason.isNotEmpty()) {
                            Toast.makeText(context, "Đã gửi báo cáo. Cảm ơn bạn!", Toast.LENGTH_SHORT).show()
                            onBackClick()
                        } else {
                            Toast.makeText(context, "Vui lòng chọn lý do", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text("Gửi báo cáo", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(100.dp))
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