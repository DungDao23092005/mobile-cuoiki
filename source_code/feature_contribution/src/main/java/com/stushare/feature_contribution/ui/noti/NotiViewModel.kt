package com.stushare.feature_contribution.ui.noti

import android.app.Application
import android.text.format.DateUtils // <-- THÊM IMPORT NÀY
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.stushare.feature_contribution.db.AppDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotiViewModel(application: Application) : AndroidViewModel(application) {
    private val notificationDao = AppDatabase.getInstance(application).notificationDao()

    val notifications: StateFlow<List<NotificationItem>> =
        notificationDao.getAllNotifications()
            .map { entities ->
                entities.map { entity ->
                    NotificationItem(
                        id = entity.id.toString(),
                        title = entity.title,
                        message = entity.message,

                        time = convertTimestampToRelativeTime(entity.timestamp),

                        type = NotificationItem.Type.valueOf(entity.type)
                    )
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    private fun convertTimestampToRelativeTime(timestamp: Long): String {
        val now = System.currentTimeMillis()
        return DateUtils.getRelativeTimeSpanString(
            timestamp,
            now,
            DateUtils.SECOND_IN_MILLIS
        ).toString()
    }

    fun markAllNotificationsAsRead() {
        viewModelScope.launch {
            notificationDao.markAllAsRead()
        }
    }
}