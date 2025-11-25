package com.stushare.feature_contribution.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun SearchScreen() {
    var query by remember { mutableStateOf("") }
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 16.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text(stringResource(R.string.search_hint), color = onSurfaceColor.copy(alpha = 0.6f)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = onSurfaceColor.copy(alpha = 0.6f)) },
            shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary,
                cursorColor = GreenPrimary,
                focusedTextColor = onSurfaceColor,
                unfocusedTextColor = onSurfaceColor
            ),
            singleLine = true
        )

        if (query.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Search, 
                        contentDescription = null, 
                        modifier = Modifier.size(64.dp),
                        tint = onSurfaceColor.copy(alpha = 0.3f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(stringResource(R.string.search_empty), color = onSurfaceColor.copy(alpha = 0.5f))
                }
            }
        } else {
            LazyColumn(contentPadding = PaddingValues(16.dp, bottom = 80.dp)) {
                item {
                    Text(
                        stringResource(R.string.search_result_title, query),
                        fontWeight = FontWeight.Bold,
                        color = onSurfaceColor,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
                items(5) { index ->
                    SearchResultItem(index, query)
                }
            }
        }
    }
}

@Composable
fun SearchResultItem(index: Int, query: String) {
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, androidx.compose.foundation.shape.RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("PDF", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = onSurfaceColor.copy(alpha = 0.7f))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "${stringResource(R.string.search_item_prefix)} $query #${index + 1}", 
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    color = onSurfaceColor
                )
                Text(
                    text = "${stringResource(R.string.search_author_prefix)} Nguyễn Văn A • ${index * 10} ${stringResource(R.string.downloads)}", 
                    color = onSurfaceColor.copy(alpha = 0.6f), 
                    fontSize = 12.sp
                )
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    }
}