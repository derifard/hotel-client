package com.example.hotelapp.domain.usecase

import com.example.hotelapp.domain.model.Hotel
import com.example.hotelapp.domain.repository.HotelRepository
import javax.inject.Inject

class GetHotelsUseCase @Inject constructor(
    private val repository: HotelRepository
) {
    suspend operator fun invoke(city: String? = null, maxPrice: Double? = null): List<Hotel> {
        return repository.getHotels(city, maxPrice)
    }
}