package com.example.hotelapp.data.remote.dto

import com.example.hotelapp.domain.model.Booking
import com.google.gson.annotations.SerializedName

data class BookingDto(
    @SerializedName("id") val id: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("hotelId") val hotelId: Int,
    @SerializedName("checkIn") val checkIn: String,
    @SerializedName("checkOut") val checkOut: String,
    @SerializedName("totalPrice") val totalPrice: Double
) {
    fun toDomain() = Booking(
        id = id,
        userId = userId,
        hotelId = hotelId,
        checkIn = checkIn,
        checkOut = checkOut,
        totalPrice = totalPrice
    )
}