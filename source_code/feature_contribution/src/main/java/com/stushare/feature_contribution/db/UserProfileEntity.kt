package com.stushare.feature_contribution.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val userId: String, // ID của người dùng từ server (ví dụ: "user_001")
    val fullName: String,
    val email: String,
    val phone: String?,
    val dateOfBirth: String?, // Lưu dạng "01/01/2000" như trên UI
    val gender: String?      // Lưu dạng "Nam" hoặc "Nữ"
)