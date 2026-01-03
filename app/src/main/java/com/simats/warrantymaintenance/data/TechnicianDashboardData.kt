package com.simats.warrantymaintenance.data

data class TechnicianDashboardData(
    val assigned: Int,
    val completed: Int,
    val today: Int,
    val todaysSchedule: List<TodaysSchedule>,
    val assignedTasks: List<AssignedTask>,
    val completedTasks: List<CompletedTask>
)

data class TodaysSchedule(
    val applianceName: String,
    val issueDescription: String,
    val customerName: String,
    val address: String,
    val time: String
)

data class AssignedTask(
    val id: Int,
    val applianceName: String,
    val issueDescription: String,
    val address: String,
    val dateTime: String
)

data class CompletedTask(
    val applianceName: String,
    val issueDescription: String,
    val date: String
)
