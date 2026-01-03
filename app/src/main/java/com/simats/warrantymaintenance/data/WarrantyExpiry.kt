package com.simats.warrantymaintenance.data

data class WarrantyExpiry(
    val id: Int,
    val applianceName: String,
    val ownerName: String,
    val expiryDate: String,
    val daysLeft: Int
)
