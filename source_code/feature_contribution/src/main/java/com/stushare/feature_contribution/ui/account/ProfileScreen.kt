package com.stushare.feature_contribution.ui.account

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stushare.feature_contribution.R
import com.stushare.feature_contribution.ui.theme.GreenPrimary

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onOpenSettings: () -> Unit,
    onOpenLeaderboard: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val publishedDocs by viewModel.publishedDocuments.collectAsStateWithLifecycle()
    val savedDocs by viewModel.savedDocuments.collectAsStateWithLifecycle()
    val downloadedDocs by viewModel.downloadedDocuments.collectAsStateWithLifecycle()

    val savedDocItems = remember(savedDocs) { savedDocs.map { DocItem(it.documentId, it.title, it.metaInfo) } }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    
    // Strings
    val tabTitles = listOf(
        stringResource(R.string.profile_tab_posted),
        stringResource(R.string.profile_tab_saved),
        stringResource(R.string.profile_tab_downloaded)
    )

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        val guestName = stringResource(R.string.profile_guest)
        val userName = if (userProfile != null) stringResource(R.string.profile_hello, userProfile!!.fullName) else stringResource(R.string.profile_hello, guestName)

        ProfileHeader(
            userName = userName,
            subText = stringResource(R.string.profile_dept),
            onSettingsClick = onOpenSettings,
            onLeaderboardClick = onOpenLeaderboard
        )

        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = GreenPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]), color = GreenPrimary)
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title, fontWeight = FontWeight.SemiBold) }
                )
            }
        }

        val currentList = when (selectedTabIndex) {
            0 -> publishedDocs
            1 -> savedDocItems
            2 -> downloadedDocs
            else -> emptyList()
        }

        if (currentList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = stringResource(R.string.profile_empty_list), color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), fontSize = 16.sp)
            }
        } else {
            LazyColumn(contentPadding = PaddingValues(16.dp, bottom = 80.dp)) {
                items(currentList) { doc ->
                    DocItemRow(item = doc, isDeletable = selectedTabIndex == 0, onDelete = { viewModel.deletePublishedDocument(doc.documentId) })
                }
            }
        }
    }
}

@Composable
fun ProfileHeader(userName: String, subText: String, onSettingsClick: () -> Unit, onLeaderboardClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.padding(bottom = 1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp).padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_person),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)),
                modifier = Modifier.size(64.dp).background(MaterialTheme.colorScheme.surfaceVariant, CircleShape).padding(12.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = userName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(text = subText, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    onClick = onLeaderboardClick,
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFFF3E0), // Giữ màu cam nhạt này hoặc đổi sang dynamic
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Text(text = stringResource(R.string.profile_view_leaderboard), color = Color(0xFFFF9800), fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }
            IconButton(onClick = onSettingsClick) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
        }
    }
}

@Composable
fun DocItemRow(item: DocItem, isDeletable: Boolean, onDelete: () -> Unit) {
    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val deleteSuccessMsg = stringResource(R.string.profile_delete_success)
    val notSupportMsg = stringResource(R.string.feature_not_supported)
    val deleteStr = stringResource(R.string.delete)

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.docTitle, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
                Text(text = item.meta, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), fontSize = 13.sp)
            }
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    if (isDeletable) {
                        DropdownMenuItem(
                            text = { Text(deleteStr) },
                            onClick = { onDelete(); showMenu = false; Toast.makeText(context, deleteSuccessMsg, Toast.LENGTH_SHORT).show() },
                            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null) }
                        )
                    } else {
                        DropdownMenuItem(text = { Text(notSupportMsg) }, onClick = { showMenu = false })
                    }
                }
            }
        }
    }
}