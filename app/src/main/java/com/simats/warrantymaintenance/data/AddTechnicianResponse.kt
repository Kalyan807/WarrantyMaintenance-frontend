package com.simats.warrantymaintenance.data

data class AddTechnicianResponse(
    val status: String,
    val message: String,
    val errors: List<String>? = null
)