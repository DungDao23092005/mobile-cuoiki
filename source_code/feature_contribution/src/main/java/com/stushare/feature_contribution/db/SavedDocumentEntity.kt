package com.stushare.feature_contribution.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_documents")
data class SavedDocumentEntity(
    @PrimaryKey
    val documentId: String = "",
    val title: String = "",
    val author: String = "",
    val subject: String = "",
    val metaInfo: String = "",
    val addedTimestamp: Long = System.currentTimeMillis(),
    val downloadCount: Int = 0
) {
    // Firestore và Room cần constructor rỗng, việc cung cấp giá trị mặc định
    // cho tất cả tham số trong constructor chính sẽ tự động tạo ra constructor rỗng này.
}