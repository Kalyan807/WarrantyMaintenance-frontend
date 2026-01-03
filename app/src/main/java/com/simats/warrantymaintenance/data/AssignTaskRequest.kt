package com.simats.warrantymaintenance.data

data class AssignTaskRequest(
    val issueId: Int,
    val technicianId: Int? = null,
    val scheduleDateTime: String? = null,
    val notes: String? = null,
    val status: String? = null
)

