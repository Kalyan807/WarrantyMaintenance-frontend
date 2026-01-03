package com.simats.warrantymaintenance.data

data class AddWarrantyResponse(
    val status: String,
    val message: String,
    val errors: List<String>? = null
)