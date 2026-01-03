package com.simats.warrantymaintenance.data

data class AnalyticsData(
    val totalIssues: Int,
    val issuesChange: Int,
    val resolvedIssues: Int,
    val resolutionRate: Int,
    val issuesByStatus: IssuesByStatus,
    val technicianPerformance: List<TechnicianPerformance>,
    val monthlyServiceCount: List<Int>
)

data class IssuesByStatus(
    val completed: Int,
    val inProgress: Int,
    val pending: Int
)

data class TechnicianPerformance(
    val name: String,
    val tasksCompleted: Int,
    val rating: Double
)
