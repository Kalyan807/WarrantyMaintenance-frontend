package com.simats.warrantymaintenance.data

data class LoginResponse(
    val status: String,
    val message: String,
    val user: User?
)

data class User(
    val id: Int,
    val full_name: String,
    val email: String
)