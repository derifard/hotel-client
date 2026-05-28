package com.example.hotelapp.domain.repository

import com.example.hotelapp.domain.model.AuthResult

interface AuthRepository {
    suspend fun login(email: String, password: String): AuthResult
    suspend fun register(email: String, password: String, name: String): AuthResult
    suspend fun getToken(): String?
    suspend fun saveToken(token: String)
    suspend fun logout()
    suspend fun getUserName(): String
    suspend fun getUserEmail(): String
    suspend fun getUserPhone(): String
    suspend fun saveUserProfile(name: String, email: String, phone: String)
}