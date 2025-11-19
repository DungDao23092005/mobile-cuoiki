package com.stushare.feature_contribution.db

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * Repository này là "Single Source of Truth".
 * Nó quản lý cả hai nguồn dữ liệu: Firestore (remote) và Room (local).
 */
class DocumentRepository(private val documentDao: SavedDocumentDao) {

    private val firestoreDb = Firebase.firestore
    private val documentsCollection = firestoreDb.collection("documents")

    /**
     * Nguồn dữ liệu cho UI. UI SẼ LUÔN LẤY DATA TỪ ĐÂY.
     * Đây là Flow đọc trực tiếp từ Room.
     */
    val allDocuments: Flow<List<SavedDocumentEntity>> = documentDao.getAllSavedDocuments()

    /**
     * Ghi dữ liệu:
     * 1. Upload lên Firestore.
     * 2. Nếu thành công, lưu vào Room.
     */
    suspend fun uploadDocument(doc: SavedDocumentEntity) {
        // 1. Gửi lên Firestore
        // .add() sẽ tạo ra một document ID ngẫu nhiên
        val documentRef = documentsCollection.add(doc).await()
        Log.d("DocumentRepository", "DocumentSnapshot added with ID: ${documentRef.id}")

        // 2. Cập nhật ID từ Firestore vào object (quan trọng) và lưu vào Room
        // Việc này giúp đồng bộ ID giữa local và server
        val docWithRemoteId = doc.copy(documentId = documentRef.id)
        documentDao.saveDocument(docWithRemoteId)
    }

    /**
     * Xóa dữ liệu:
     * 1. Xóa trên Firestore.
     * 2. Nếu thành công, xóa trên Room.
     */
    suspend fun deleteDocument(docId: String) {
        // 1. Xóa trên Firestore
        documentsCollection.document(docId).delete().await()

        // 2. Xóa trên Room
        documentDao.unsaveDocument(docId)
    }


    /**
     * Bắt đầu lắng nghe thay đổi từ Firestore (Real-time sync).
     * Khi có gì mới trên server, hàm này tự động chạy và cập nhật vào Room.
     * Vì UI đang theo dõi Room qua `allDocuments`, UI sẽ tự động update.
     */
    fun startListeningForRemoteChanges(scope: CoroutineScope) {
        documentsCollection.addSnapshotListener { snapshots, e ->
            if (e != null) {
                Log.w("DocumentRepository", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshots != null) {
                // Chuyển đổi snapshots thành danh sách Entity
                // Lưu ý: SavedDocumentEntity cần có constructor mặc định (data class đã có sẵn)
                val remoteDocuments = snapshots.toObjects<SavedDocumentEntity>()

                // Dùng coroutine để ghi vào CSDL Room
                scope.launch {
                    Log.d("DocumentRepository", "Syncing ${remoteDocuments.size} docs from Firestore...")

                    // Lưu tất cả tài liệu mới vào Room
                    // OnConflictStrategy.REPLACE trong DAO sẽ tự động ghi đè nếu trùng ID
                    remoteDocuments.forEach { doc ->
                        // Cần đảm bảo documentId được set đúng từ Firestore ID nếu object chưa có
                        // Tuy nhiên ở bước upload ta đã set rồi.
                        // Khi lấy về, toObjects sẽ cố gắng map field, nếu ID nằm trong document, nó sẽ được map.
                        documentDao.saveDocument(doc)
                    }
                }
            }
        }
    }
}