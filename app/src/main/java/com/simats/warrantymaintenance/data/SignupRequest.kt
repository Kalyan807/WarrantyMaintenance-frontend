package com.simats.warrantymaintenance.data

data class SignupRequest(
    val full_name: String,
    val email: String,
    val phone: String,
    val address: String,
    val password: String,
    val confirm_password: String,
    val role: String
)