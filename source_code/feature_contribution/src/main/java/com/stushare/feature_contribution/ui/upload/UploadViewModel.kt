package com.stushare.feature_contribution.ui.upload

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.stushare.feature_contribution.db.AppDatabase
import com.stushare.feature_contribution.db.DocumentRepository // Import Repository
import com.stushare.feature_contribution.db.NotificationEntity
import com.stushare.feature_contribution.db.SavedDocumentEntity
import com.stushare.feature_contribution.ui.noti.NotificationItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UploadViewModel(application: Application) : AndroidViewModel(application) {

    // Khởi tạo Database
    private val database = AppDatabase.getInstance(application)
    private val notificationDao = database.notificationDao()

    // Khởi tạo Repository (thay vì dùng trực tiếp DAO)
    private val documentRepository = DocumentRepository(database.savedDocumentDao())

    private val _isUploading = MutableStateFlow(false)
    val isUploading = _isUploading.asStateFlow()

    private val _uploadEvent = MutableSharedFlow<UploadResult>()
    val uploadEvent = _uploadEvent.asSharedFlow()

    sealed class UploadResult {
        data class Success(val message: String) : UploadResult()
        data class Error(val message: String) : UploadResult()
    }

    fun handleUploadClick(title: String, description: String?) {
        viewModelScope.launch {
            _isUploading.value = true
            try {
                // --- BẮT ĐẦU LOGIC ---

                // 1. Giả lập delay (nếu muốn test UI loading)
                // delay(1000)

                // 2. TẠO OBJECT TÀI LIỆU
                // documentId tạm thời để trống, Repository sẽ cập nhật ID thật từ Firestore sau khi upload
                val newDocument = SavedDocumentEntity(
                    documentId = "",
                    title = title,
                    author = "Người dùng hiện tại", // TODO: Thay bằng tên thật từ User Profile
                    subject = "Môn học chung",
                    metaInfo = "0 lượt tải · Môn học chung",
                    addedTimestamp = System.currentTimeMillis()
                )

                // 3. GỌI REPOSITORY ĐỂ UPLOAD
                // Repository sẽ lo việc gửi lên Firestore VÀ lưu vào Room
                documentRepository.uploadDocument(newDocument)

                // 4. TẠO VÀ LƯU THÔNG BÁO (Logic local - chỉ lưu trên máy này)
                val newNotification = NotificationEntity(
                    title = "Tải lên thành công",
                    message = "Tài liệu: $title",
                    timestamp = System.currentTimeMillis(),
                    type = NotificationItem.Type.SUCCESS.name,
                    isRead = false
                )
                notificationDao.addNotification(newNotification)

                // --- KẾT THÚC LOGIC ---

                _uploadEvent.emit(UploadResult.Success("Upload tài liệu thành công"))

            } catch (e: Exception) {
                _uploadEvent.emit(UploadResult.Error(e.message ?: "Đã xảy ra lỗi khi upload"))
            } finally {
                _isUploading.value = false
            }
        }
    }
}