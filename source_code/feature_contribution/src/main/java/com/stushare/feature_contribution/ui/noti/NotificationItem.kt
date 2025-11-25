package com.stushare.feature_contribution.ui.noti

data class NotificationItem(
    val id: String = System.currentTimeMillis().toString(),
    val title: String,
    val message: String,
    val time: String = "Hôm nay",
    val type: Type = Type.INFO
) {
    enum class Type { INFO, SUCCESS, WARNING }
}