package com.sejuma.hospitalapplication.model

data class Nurse(
    val nurseId: Int,
    val name: String,
    val password: String,
    val user: String,
    val imageFile: String,
)

data class LoginRequest(
    val user: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val user: String,
    val password: String,
)
