package com.simats.warrantymaintenance.data

data class TaskDetails(
    val id: Int = 0,
    val applianceName: String,
    val issueDescription: String,
    val priority: String,
    val address: String,
    val customerName: String,
    val customerPhone: String,
    val supervisorNotes: String
)

