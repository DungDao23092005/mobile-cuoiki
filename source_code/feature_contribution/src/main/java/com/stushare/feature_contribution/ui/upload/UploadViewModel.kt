// trong com.stushare.feature_contribution.ui.upload
package com.stushare.feature_contribution.ui.upload

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.stushare.feature_contribution.db.AppDatabase
import com.stushare.feature_contribution.db.NotificationEntity
import com.stushare.feature_contribution.ui.noti.NotificationItem
// THÊM: Import Firebase và Coroutines
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

// 1. Kế thừa từ AndroidViewModel để lấy Application context
class UploadViewModel(application: Application) : AndroidViewModel(application) {

    // 2. Khởi tạo Database và DAO
    private val database = AppDatabase.getInstance(application)
    private val notificationDao = database.notificationDao()
    // THÊM: Khởi tạo Firestore
    private val firestoreDb = Firebase.firestore

    // 3. Quản lý trạng thái loading
    private val _isUploading = MutableStateFlow(false)
    val isUploading = _isUploading.asStateFlow()

    // 4. Gửi sự kiện (event) một lần về Fragment (như Toast)
    private val _uploadEvent = MutableSharedFlow<UploadResult>()
    val uploadEvent = _uploadEvent.asSharedFlow()

    // Lớp sealed để định nghĩa các kết quả có thể xảy ra
    sealed class UploadResult {
        data class Success(val message: String) : UploadResult()
        data class Error(val message: String) : UploadResult()
    }

    /**
     * Hàm helper để chuyển thao tác Firebase bất đồng bộ thành suspend function.
     */
    private suspend fun saveDocumentMetadataToFirestore(
        title: String,
        description: String?
    ): DocumentReference = suspendCoroutine { continuation ->
        val documentMetadata = hashMapOf(
            "title" to title,
            "description" to description,
            "authorId" to "user_001", // Giả định: Thay bằng Auth User ID thật sau
            "uploadTime" to System.currentTimeMillis()
        )

        firestoreDb.collection("documents")
            .add(documentMetadata)
            .addOnSuccessListener { docRef ->
                // Trả về DocumentReference khi thành công
                continuation.resume(docRef)
            }
            .addOnFailureListener { e ->
                // Gửi ngoại lệ nếu thất bại
                continuation.resumeWithException(e)
            }
    }

    /**
     * Hàm này được gọi từ Fragment khi bấm nút Upload.
     */
    fun handleUploadClick(title: String, description: String?) {
        // Chạy logic trong viewModelScope
        viewModelScope.launch {
            _isUploading.value = true
            try {
                // --- BẮT ĐẦU LOGIC NGHIỆP VỤ ---

                // 1. Giả lập việc upload file lên Storage (3s)
                delay(3000)

                // 2. LƯU METADATA LÊN FIRESTORE (Dùng suspend fun)
                val docRef = saveDocumentMetadataToFirestore(title, description)

                // 3. TẠO VÀ LƯU THÔNG BÁO VÀO ROOM DATABASE
                val newNotification = NotificationEntity(
                    title = "Tải lên thành công",
                    message = "Tài liệu: $title (ID: ${docRef.id})",
                    timeText = "Hôm nay",
                    type = NotificationItem.Type.SUCCESS.name,
                    isRead = false
                )
                notificationDao.addNotification(newNotification)

                // --- KẾT THÚC LOGIC NGHIỆP VỤ ---

                // 4. Gửi sự kiện thành công về UI
                _uploadEvent.emit(UploadResult.Success("Upload và lưu metadata thành công!"))

            } catch (e: Exception) {
                // 5. Xử lý lỗi từ bất kỳ bước nào (delay hoặc Firestore)
                _uploadEvent.emit(UploadResult.Error(e.message ?: "Đã xảy ra lỗi khi tải lên"))
            } finally {
                // 6. Luôn tắt trạng thái loading
                _isUploading.value = false
            }
        }
    }
}