package com.simats.warrantymaintenance.data

data class Technician(
    val id: Int,
    val name: String,
    val specialization: String,
    val experience: Int,
    val status: String,
    val rating: Double
)
