package com.stushare.feature_contribution.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stushare.feature_contribution.R
import com.stushare.feature_contribution.ui.theme.GreenPrimary

@Composable
fun HomeScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background), // Nền động
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        color = GreenPrimary,
                        shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(24.dp).padding(top = 20.dp)) {
                    Text(
                        text = stringResource(R.string.home_welcome), // String resource
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.home_subtitle),
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 16.sp
                    )
                }
            }
        }

        item {
            Text(
                text = stringResource(R.string.home_popular_subjects),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground, // Chữ động
                modifier = Modifier.padding(16.dp)
            )
        }

        items(6) { index ->
            SubjectCard(index)
        }
    }
}

@Composable
fun SubjectCard(index: Int) {
    val subjectName = when(index) {
        0 -> stringResource(R.string.subject_mobile)
        1 -> stringResource(R.string.subject_dsa)
        2 -> stringResource(R.string.subject_ai)
        3 -> stringResource(R.string.subject_network)
        4 -> stringResource(R.string.subject_db)
        else -> stringResource(R.string.subject_english)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), // Thẻ động
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = RoundedCornerShape(12.dp),
                color = GreenPrimary.copy(alpha = 0.1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = GreenPrimary,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = subjectName, 
                    fontWeight = FontWeight.Bold, 
                    fontSize = 16.sp, 
                    color = MaterialTheme.colorScheme.onSurface // Chữ động
                )
                Text(
                    text = "${(10..500).random()} ${stringResource(R.string.home_docs_count)}", 
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), 
                    fontSize = 14.sp
                )
            }
        }
    }
}