package com.stushare.feature_contribution.ui.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.stushare.feature_contribution.db.AppDatabase
import com.stushare.feature_contribution.db.SavedDocumentEntity
import com.stushare.feature_contribution.db.UserProfileEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = AppDatabase.getInstance(application).userDao()
    private val savedDocumentDao = AppDatabase.getInstance(application).savedDocumentDao()

    private val USER_ID = "user_001"

    val userProfile: StateFlow<UserProfileEntity?> = userDao.getProfile(USER_ID)
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    val publishedDocuments: StateFlow<List<DocItem>> = savedDocumentDao.getAllSavedDocuments()
        .map { entities ->
            entities.map { entity ->
                DocItem(
                    documentId = entity.documentId,
                    docTitle = entity.title,
                    meta = entity.metaInfo
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    val savedDocuments: StateFlow<List<SavedDocumentEntity>> = MutableStateFlow(emptyList())
    val downloadedDocuments: StateFlow<List<DocItem>> = MutableStateFlow(
        listOf(
            // *** SỬA ĐỔI Ở ĐÂY (thêm ID giả) ***
            DocItem("download_1", "Tài liệu đã tải 1", "Tác giả A · Mobile"),
            DocItem("download_2", "Tài liệu đã tải 2", "Tác giả B · Web")
        )
    )
    fun deletePublishedDocument(documentId: String) {
        viewModelScope.launch {
            savedDocumentDao.unsaveDocument(documentId)
        }
    }


    init {
        viewModelScope.launch {
            if (userDao.getProfile(USER_ID).stateIn(viewModelScope).value == null) {
                userDao.upsertProfile(
                    UserProfileEntity(
                        userId = USER_ID,
                        fullName = "Dũng Đào",
                        email = "dungdao@test.com",
                        phone = "0123456789",
                        dateOfBirth = "01/01/2000",
                        gender = "Nam"
                    )
                )
            }
        }
    }
}