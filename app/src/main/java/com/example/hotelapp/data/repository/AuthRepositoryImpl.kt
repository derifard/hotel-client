package com.example.hotelapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.hotelapp.data.remote.api.HotelApi
import com.example.hotelapp.data.remote.dto.LoginRequestDto
import com.example.hotelapp.data.remote.dto.RegisterRequestDto
import com.example.hotelapp.domain.model.AuthResult
import com.example.hotelapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: HotelApi,
    private val dataStore: DataStore<Preferences>
) : AuthRepository {

    private val tokenKey = stringPreferencesKey("jwt_token")
    private val nameKey = stringPreferencesKey("user_name")
    private val emailKey = stringPreferencesKey("user_email")
    private val phoneKey = stringPreferencesKey("user_phone")

    override suspend fun login(email: String, password: String): AuthResult {
        val response = api.login(LoginRequestDto(email, password))
        saveToken(response.token)
        dataStore.edit {
            it[emailKey] = email
            it[nameKey] = response.user.name
        }
        return response.toDomain()
    }

    override suspend fun register(email: String, password: String, name: String): AuthResult {
        val response = api.register(RegisterRequestDto(email, password, name))
        saveToken(response.token)
        dataStore.edit {
            it[emailKey] = email
            it[nameKey] = name
        }
        return response.toDomain()
    }

    override suspend fun getToken(): String? {
        return dataStore.data.map { it[tokenKey] }.first()
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit { it[tokenKey] = token }
    }

    override suspend fun logout() {
        dataStore.edit { it.clear() }
    }

    override suspend fun getUserName(): String {
        return dataStore.data.map { it[nameKey] ?: "" }.first()
    }

    override suspend fun getUserEmail(): String {
        return dataStore.data.map { it[emailKey] ?: "" }.first()
    }

    override suspend fun getUserPhone(): String {
        return dataStore.data.map { it[phoneKey] ?: "" }.first()
    }

    override suspend fun saveUserProfile(name: String, email: String, phone: String) {
        dataStore.edit {
            it[nameKey] = name
            it[emailKey] = email
            it[phoneKey] = phone
        }
    }
}