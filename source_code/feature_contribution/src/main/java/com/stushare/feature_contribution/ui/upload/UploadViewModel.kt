// trong com.stushare.feature_contribution.ui.upload
package com.stushare.feature_contribution.ui.upload

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.stushare.feature_contribution.db.AppDatabase
import com.stushare.feature_contribution.db.NotificationEntity
import com.stushare.feature_contribution.ui.noti.NotificationItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 1. Kế thừa từ AndroidViewModel để lấy Application context
class UploadViewModel(application: Application) : AndroidViewModel(application) {

    // 2. Khởi tạo Database và DAO
    private val database = AppDatabase.getInstance(application)
    private val notificationDao = database.notificationDao()

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
     * Hàm này được gọi từ Fragment khi bấm nút Upload.
     */
    fun handleUploadClick(title: String, description: String?) {
        // Chạy logic trong viewModelScope
        viewModelScope.launch {
            _isUploading.value = true
            try {
                // --- BẮT ĐẦU LOGIC NGHIỆP VỤ ---

                // 1. Giả lập việc upload (thay thế bằng logic upload thật sau)
                delay(3000)

                // 2. TẠO VÀ LƯU THÔNG BÁO VÀO ROOM DATABASE
                val newNotification = NotificationEntity(
                    title = "Tải lên thành công",
                    message = "Tài liệu: $title",
                    timeText = "Hôm nay", // Bạn có thể dùng SimpleDateFormat để lấy giờ hiện tại
                    type = NotificationItem.Type.SUCCESS.name, // Lưu tên của Enum (ví dụ: "SUCCESS")
                    isRead = false
                )

                // Ghi vào database (suspend function)
                notificationDao.addNotification(newNotification)

                // --- KẾT THÚC LOGIC NGHIỆP VỤ ---

                // 3. Gửi sự kiện thành công về UI
                _uploadEvent.emit(UploadResult.Success("Upload thành công"))

            } catch (e: Exception) {
                // 4. Gửi sự kiện lỗi về UI nếu có
                _uploadEvent.emit(UploadResult.Error(e.message ?: "Đã xảy ra lỗi"))
            } finally {
                // 5. Luôn tắt trạng thái loading
                _isUploading.value = false
            }
        }
    }
}