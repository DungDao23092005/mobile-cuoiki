package com.stushare.feature_contribution.ui.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.stushare.feature_contribution.db.AppDatabase
import com.stushare.feature_contribution.db.DocumentRepository // Import Repository
import com.stushare.feature_contribution.db.SavedDocumentEntity
import com.stushare.feature_contribution.db.UserProfileEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = AppDatabase.getInstance(application).userDao()

    // Khởi tạo Repository
    private val documentRepository = DocumentRepository(AppDatabase.getInstance(application).savedDocumentDao())

    private val USER_ID = "user_001"

    val userProfile: StateFlow<UserProfileEntity?> = userDao.getProfile(USER_ID)
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // ĐỌC DỮ LIỆU TỪ REPOSITORY (Room được đồng bộ)
    val publishedDocuments: StateFlow<List<DocItem>> = documentRepository.allDocuments
        .map { entities ->
            entities.map { entity ->
                DocItem(
                    documentId = entity.documentId,
                    docTitle = entity.title,
                    meta = entity.metaInfo
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Các biến này giữ nguyên logic cũ của bạn (có thể nâng cấp sau)
    val savedDocuments: StateFlow<List<SavedDocumentEntity>> = MutableStateFlow(emptyList())
    val downloadedDocuments: StateFlow<List<DocItem>> = MutableStateFlow(
        listOf(
            DocItem("download_1", "Tài liệu đã tải 1", "Tác giả A · Mobile"),
            DocItem("download_2", "Tài liệu đã tải 2", "Tác giả B · Web")
        )
    )

    fun deletePublishedDocument(documentId: String) {
        viewModelScope.launch {
            // Sử dụng Repository để xóa (xóa cả Firestore và Room)
            try {
                documentRepository.deleteDocument(documentId)
            } catch (e: Exception) {
                // Có thể thêm logic xử lý lỗi nếu cần
            }
        }
    }

    init {
        // QUAN TRỌNG: Bắt đầu lắng nghe thay đổi từ Firestore ngay khi ViewModel khởi tạo
        // Việc này đảm bảo dữ liệu Room luôn được cập nhật mới nhất từ server
        documentRepository.startListeningForRemoteChanges(viewModelScope)

        // Logic tạo profile mẫu (giữ nguyên)
        viewModelScope.launch {
            if (userDao.getProfile(USER_ID).stateIn(viewModelScope).value == null) {
                userDao.upsertProfile(
                    UserProfileEntity(
                        userId = USER_ID,
                        fullName = "Dũng Đào",
                        email = "dungdao@test.com",
                        phone = "0123456789",
                        dateOfBirth = "01/01/2000",
                        gender = "Nam"
                    )
                )
            }
        }
    }
}