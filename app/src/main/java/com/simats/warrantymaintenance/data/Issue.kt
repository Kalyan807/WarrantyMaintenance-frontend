package com.simats.warrantymaintenance.data

data class Issue(
    val id: Int,
    val applianceName: String,
    val issueDescription: String,
    val priority: String,
    val status: String,
    val reportedBy: String,
    val reportedDate: String,
    val location: String = ""
)

