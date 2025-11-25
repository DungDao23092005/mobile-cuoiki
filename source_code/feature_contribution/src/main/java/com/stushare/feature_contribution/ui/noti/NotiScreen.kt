package com.stushare.feature_contribution.ui.noti

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stushare.feature_contribution.R
import com.stushare.feature_contribution.ui.theme.BlueInfo
import com.stushare.feature_contribution.ui.theme.GreenPrimary
import com.stushare.feature_contribution.ui.theme.OrangeWarning

@Composable
fun NotiScreen(viewModel: NotiViewModel) {
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Màu nền động
    ) {
        Text(
            text = stringResource(R.string.noti_header),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground, // Màu chữ động
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(contentPadding = PaddingValues(bottom = 80.dp)) {
            items(notifications) { item ->
                NotificationItemRow(item)
            }
        }
    }
}

@Composable
fun NotificationItemRow(item: NotificationItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), // Màu thẻ động
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            val (iconRes, iconColor) = when (item.type) {
                NotificationItem.Type.SUCCESS -> Pair(R.drawable.ic_check_circle, GreenPrimary)
                NotificationItem.Type.WARNING -> Pair(R.drawable.ic_warning, OrangeWarning)
                else -> Pair(R.drawable.ic_notifications, BlueInfo)
            }

            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = item.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface, // Màu chữ động
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = item.time,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.message,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}