package com.simats.warrantymaintenance.data

data class Notification(
    val id: Int,
    val title: String,
    val message: String,
    val time: String,
    val type: String,
    val isRead: Boolean
)
