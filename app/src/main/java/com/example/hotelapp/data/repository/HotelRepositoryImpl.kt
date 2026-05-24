package com.example.hotelapp.data.repository

import com.example.hotelapp.data.remote.api.HotelApi
import com.example.hotelapp.domain.model.Hotel
import com.example.hotelapp.domain.repository.HotelRepository
import javax.inject.Inject

class HotelRepositoryImpl @Inject constructor(
    private val api: HotelApi
) : HotelRepository {

    override suspend fun getHotels(city: String?, maxPrice: Double?): List<Hotel> {
        return api.getHotels(city, maxPrice).map { it.toDomain() }
    }

    override suspend fun getHotelById(id: Int): Hotel? {
        return try {
            api.getHotelById(id).toDomain()
        } catch (e: Exception) {
            null
        }
    }
}