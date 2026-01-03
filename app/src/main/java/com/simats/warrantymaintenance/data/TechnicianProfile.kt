package com.simats.warrantymaintenance.data

data class TechnicianProfile(
    val name: String,
    val specialization: String,
    val rating: Double,
    val completedTasks: Int,
    val experience: Int,
    val successRate: Int
)
