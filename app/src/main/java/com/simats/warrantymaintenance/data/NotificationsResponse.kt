package com.simats.warrantymaintenance.data

data class NotificationsResponse(
    val notifications: List<Notification>,
    val unreadCount: Int
)
