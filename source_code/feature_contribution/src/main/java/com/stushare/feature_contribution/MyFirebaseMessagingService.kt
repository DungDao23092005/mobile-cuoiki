// Đặt tại: com.stushare.feature_contribution.MyFirebaseMessagingService.kt
package com.stushare.feature_contribution

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.stushare.feature_contribution.db.AppDatabase
import com.stushare.feature_contribution.db.NotificationEntity
import com.stushare.feature_contribution.ui.noti.NotificationItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * Được gọi khi app nhận được thông báo.
     * Hàm này sẽ luôn được gọi nếu thông báo có chứa 'data' payload.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        var title: String? = null
        var body: String? = null

        // (MỚI) Ưu tiên đọc từ 'data' payload
        // Đây là cách tốt nhất để đảm bảo onMessageReceived luôn được gọi
        if (remoteMessage.data.isNotEmpty()) {
            title = remoteMessage.data["title"]
            body = remoteMessage.data["body"]
        }
        // Nếu không có 'data', đọc từ 'notification' payload
        // (Cách này chỉ hoạt động khi app đang ở Foreground)
        else if (remoteMessage.notification != null) {
            title = remoteMessage.notification!!.title
            body = remoteMessage.notification!!.body
        }

        // Nếu không có title/body (ví dụ: data ping), thì không làm gì cả
        if (title == null || body == null) {
            return
        }

        // --- Bắt đầu lưu vào Room (Giữ nguyên) ---

        // 2. TẠO VÀ LƯU VÀO ROOM DATABASE
        val newNotification = NotificationEntity(
            title = title,
            message = body,
            timestamp = System.currentTimeMillis(),
            type = NotificationItem.Type.INFO.name, // Mặc định là INFO
            isRead = false
        )

        // 3. Lấy instance của Database và lưu
        val notificationDao = AppDatabase.getInstance(applicationContext).notificationDao()
        GlobalScope.launch {
            notificationDao.addNotification(newNotification)
        }

        // 4. Hiển thị thông báo lên thanh status bar
        sendSystemNotification(title, body)
    }

    /**
     * Được gọi khi Firebase cấp cho app một token mới.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("MyFCMToken", "FCM Registration Token: $token")
    }

    /**
     * Hàm helper để tạo thông báo trên thanh Status Bar (Giữ nguyên)
     */
    private fun sendSystemNotification(title: String, message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "stushare_channel_id" // ID của kênh thông báo

        // Tạo Channel (bắt buộc từ Android 8.0 Oreo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Thông báo chung",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Tạo thông báo
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notifications) // Icon thông báo của bạn
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true) // Tự tắt khi người dùng bấm vào

        // Hiển thị thông báo
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}