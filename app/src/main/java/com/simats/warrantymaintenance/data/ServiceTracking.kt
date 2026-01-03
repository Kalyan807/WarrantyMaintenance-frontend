package com.simats.warrantymaintenance.data

data class ServiceTrackingResponse(
    val status: String,
    val service: ServiceTracking?
)

data class ServiceTracking(
    val id: Int,
    val appliance: String,
    val issue: String,
    val status: String,
    val reportedDate: String,
    val assignedDate: String?,
    val inProgressDate: String?,
    val completedDate: String?,
    val technician: AssignedTechnician?
)

data class AssignedTechnician(
    val id: Int,
    val name: String,
    val phone: String,
    val expectedVisit: String?
)
