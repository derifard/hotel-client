package com.example.hotelapp.data.remote.dto

import com.example.hotelapp.domain.model.AuthResult
import com.example.hotelapp.domain.model.User
import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String
) {
    fun toDomain() = User(id = id, email = email, name = name)
}

data class AuthResponseDto(
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: UserDto
) {
    fun toDomain() = AuthResult(token = token, user = user.toDomain())
}

data class LoginRequestDto(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class RegisterRequestDto(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: String
)

data class CreateBookingRequestDto(
    @SerializedName("hotelId") val hotelId: Int,
    @SerializedName("checkIn") val checkIn: String,
    @SerializedName("checkOut") val checkOut: String
)