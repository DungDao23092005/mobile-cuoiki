package com.stushare.feature_contribution.ui.leaderboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class RankedUser(
    val rank: Int,
    val name: String,
    val score: Int
)

data class RankedDocument(
    val rank: Int,
    val title: String,
    val author: String,
    val downloads: Int
)

class LeaderboardViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val TAG = "LeaderboardViewModel"

    private val _rankedUsers = MutableStateFlow<List<RankedUser>>(emptyList())
    val rankedUsers: StateFlow<List<RankedUser>> = _rankedUsers

    private val _rankedDocs = MutableStateFlow<List<RankedDocument>>(emptyList())
    val rankedDocs: StateFlow<List<RankedDocument>> = _rankedDocs

    fun fetchRankedUsers() {
        viewModelScope.launch {
            try {
                // Cần tạo collection 'users' trên Firestore với field: fullName, score
                val snapshot = db.collection("users")
                    .orderBy("score", Query.Direction.DESCENDING)
                    .limit(10)
                    .get()
                    .await()

                val userList = snapshot.documents.mapIndexed { index, doc ->
                    RankedUser(
                        rank = index + 1,
                        name = doc.getString("fullName") ?: "Người dùng",
                        score = (doc.getLong("score") ?: 0L).toInt()
                    )
                }
                _rankedUsers.value = userList
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching users (Firestore có thể chưa setup): ", e)
                // Dữ liệu giả lập để test UI nếu chưa có Backend
                _rankedUsers.value = listOf(
                    RankedUser(1, "Nguyễn Văn A", 1200),
                    RankedUser(2, "Trần Thị B", 1100),
                    RankedUser(3, "Lê Văn C", 980),
                    RankedUser(4, "Phạm Minh D", 870),
                    RankedUser(5, "Hoàng E", 760)
                )
            }
        }
    }

    fun fetchRankedDocs() {
        viewModelScope.launch {
            try {
                // Cần tạo collection 'documents' trên Firestore với field: title, authorName, downloadCount
                val snapshot = db.collection("documents")
                    .orderBy("downloadCount", Query.Direction.DESCENDING)
                    .limit(10)
                    .get()
                    .await()

                val docList = snapshot.documents.mapIndexed { index, doc ->
                    RankedDocument(
                        rank = index + 1,
                        title = doc.getString("title") ?: "Tài liệu",
                        author = doc.getString("authorName") ?: "N/A",
                        downloads = (doc.getLong("downloadCount") ?: 0L).toInt()
                    )
                }
                _rankedDocs.value = docList
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching docs (Firestore có thể chưa setup): ", e)
                // Dữ liệu giả lập để test UI
                _rankedDocs.value = listOf(
                    RankedDocument(1, "Đề cương Giải tích 1", "Nguyễn Văn A", 320),
                    RankedDocument(2, "Slide Cấu trúc dữ liệu", "Trần Thị B", 290),
                    RankedDocument(3, "Tài liệu Toán rời rạc", "Lê Văn C", 280),
                    RankedDocument(4, "Đề thi Xác suất TK", "Phạm Minh D", 240),
                    RankedDocument(5, "Bài giảng Java cơ bản", "Phạm Văn E", 220)
                )
            }
        }
    }
}