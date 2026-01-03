package com.simats.warrantymaintenance.data

data class SignupResponse(
    val status: String,
    val message: String,
    val user_id: Int? = null
)