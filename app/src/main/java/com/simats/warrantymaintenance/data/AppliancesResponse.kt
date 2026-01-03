package com.simats.warrantymaintenance.data

data class AppliancesResponse(
    val appliances: List<Appliance>,
    val registeredAppliances: Int
)
