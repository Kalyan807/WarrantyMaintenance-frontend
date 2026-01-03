package com.simats.warrantymaintenance.data

data class ReportIssueResponse(
    val status: String,
    val message: String,
    val errors: List<String>? = null
)
