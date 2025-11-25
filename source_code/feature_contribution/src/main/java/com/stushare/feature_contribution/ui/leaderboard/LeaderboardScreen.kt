package com.stushare.feature_contribution.ui.leaderboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stushare.feature_contribution.R
import com.stushare.feature_contribution.ui.theme.GreenPrimary
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LeaderboardScreen(
    viewModel: LeaderboardViewModel,
    onBackClick: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()
    val titles = listOf(stringResource(R.string.leaderboard_tab_users), stringResource(R.string.leaderboard_tab_docs))

    val rankedUsers by viewModel.rankedUsers.collectAsStateWithLifecycle()
    val rankedDocs by viewModel.rankedDocs.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchRankedUsers()
        viewModel.fetchRankedDocs()
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Box(
            modifier = Modifier.fillMaxWidth().height(120.dp).background(GreenPrimary).padding(top = 32.dp, start = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) { Text("🔙", fontSize = 24.sp, color = Color.White) }
                Text(text = stringResource(R.string.leaderboard_title), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(start = 8.dp))
            }
        }

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = GreenPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]), color = GreenPrimary)
            }
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(text = title, fontWeight = FontWeight.Bold) }
                )
            }
        }

        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
            when (page) {
                0 -> RankedUsersList(rankedUsers)
                1 -> RankedDocsList(rankedDocs)
            }
        }
    }
}

@Composable
fun RankedUsersList(users: List<RankedUser>) {
    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        items(users) { user -> RankItem(rank = user.rank, title = user.name, subtitle = "${user.score} ${stringResource(R.string.points)}") }
    }
}

@Composable
fun RankedDocsList(docs: List<RankedDocument>) {
    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        items(docs) { doc -> RankItem(rank = doc.rank, title = doc.title, subtitle = "${doc.author} • ${doc.downloads} ${stringResource(R.string.downloads)}") }
    }
}

@Composable
fun RankItem(rank: Int, title: String, subtitle: String) {
    val rankColor = when (rank) {
        1 -> Color(0xFFFFD700)
        2 -> Color(0xFFC0C0C0)
        3 -> Color(0xFFCD7F32)
        else -> MaterialTheme.colorScheme.onSurface
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "$rank", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = rankColor, modifier = Modifier.width(40.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                Text(text = subtitle, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), fontSize = 14.sp)
            }
        }
    }
}