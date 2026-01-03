package com.simats.warrantymaintenance.data

data class Appliance(
    val id: Int,
    val name: String,
    val type: String,
    val warrantyDaysLeft: Int,
    val imageUrl: String
)
