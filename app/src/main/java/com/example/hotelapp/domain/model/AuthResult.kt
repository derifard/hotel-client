package com.example.hotelapp.domain.model

data class AuthResult(
    val token: String,
    val user: User
)