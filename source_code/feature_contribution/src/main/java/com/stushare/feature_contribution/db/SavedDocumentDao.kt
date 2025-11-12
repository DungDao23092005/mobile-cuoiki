// trong com.stushare.feature_contribution.db
package com.stushare.feature_contribution.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedDocumentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDocument(doc: SavedDocumentEntity)

    @Query("SELECT * FROM saved_documents ORDER BY addedTimestamp DESC")
    fun getAllSavedDocuments(): Flow<List<SavedDocumentEntity>>

    @Query("DELETE FROM saved_documents WHERE documentId = :docId")
    suspend fun unsaveDocument(docId: String)
}