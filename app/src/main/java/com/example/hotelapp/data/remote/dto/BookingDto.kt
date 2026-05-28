package com.example.hotelapp.data.remote.dto

import com.example.hotelapp.domain.model.Booking
import com.google.gson.annotations.SerializedName

data class BookingDto(
    @SerializedName("id") val id: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("hotelId") val hotelId: Int,
    @SerializedName("hotelName") val hotelName: String = "",
    @SerializedName("hotelCity") val hotelCity: String = "",
    @SerializedName("checkIn") val checkIn: String,
    @SerializedName("checkOut") val checkOut: String,
    @SerializedName("totalPrice") val totalPrice: Double
) {
    fun toDomain() = Booking(
        id = id,
        userId = userId,
        hotelId = hotelId,
        hotelName = hotelName,
        hotelCity = hotelCity,
        checkIn = checkIn,
        checkOut = checkOut,
        totalPrice = totalPrice
    )
}