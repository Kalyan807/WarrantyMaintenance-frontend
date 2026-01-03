package com.simats.warrantymaintenance.data

data class DashboardSummary(
    val totalAppliances: Int,
    val totalTechnicians: Int,
    val pendingIssues: Int,
    val warrantyExpiry: Int
)