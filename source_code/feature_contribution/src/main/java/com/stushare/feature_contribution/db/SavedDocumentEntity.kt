package com.stushare.feature_contribution.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_documents")
data class SavedDocumentEntity(
    @PrimaryKey
    val documentId: String, // ID của tài liệu từ server
    val title: String,
    val author: String, // "Tác giả" từ màn hình Form
    val subject: String,  // "Môn học" từ màn hình Form
    val metaInfo: String, // Dòng nhỏ (ví dụ: "10 lượt tải · Môn học")
    val addedTimestamp: Long = System.currentTimeMillis() // Để sắp xếp
)