package com.stushare.feature_contribution.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String, // "Chúc mừng!"
    val message: String, // "Bạn đã đạt 50 điểm đóng góp"
    val timestamp: Long,
    val type: String, // Để phân biệt icon (ví dụ: "system", "like", "milestone")
    val isRead: Boolean = false
)