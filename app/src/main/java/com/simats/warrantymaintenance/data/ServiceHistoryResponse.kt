package com.simats.warrantymaintenance.data

data class ServiceHistoryResponse(
    val serviceHistory: List<ServiceHistory>,
    val totalServices: Int
)
