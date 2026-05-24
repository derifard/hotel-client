package com.example.hotelapp.data.remote.dto

import com.example.hotelapp.domain.model.Hotel
import com.google.gson.annotations.SerializedName

data class HotelDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("city") val city: String,
    @SerializedName("country") val country: String,
    @SerializedName("description") val description: String,
    @SerializedName("pricePerNight") val pricePerNight: Double,
    @SerializedName("rating") val rating: Double,
    @SerializedName("imageUrl") val imageUrl: String?
) {
    fun toDomain() = Hotel(
        id = id,
        name = name,
        city = city,
        country = country,
        description = description,
        pricePerNight = pricePerNight,
        rating = rating,
        imageUrl = imageUrl
    )
}