package com.sejuma.hospitalapplication.model

data class Nurse(
    val nurseId: Int,
    val name: String,
    val user: String,
    val password: String,
    //val imageRes: Int,
)

data class LoginRequest(
    val user: String,
    val password: String
)