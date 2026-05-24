package com.example.hotelapp.data.remote.api

import com.example.hotelapp.data.remote.dto.AuthResponseDto
import com.example.hotelapp.data.remote.dto.BookingDto
import com.example.hotelapp.data.remote.dto.CreateBookingRequestDto
import com.example.hotelapp.data.remote.dto.HotelDto
import com.example.hotelapp.data.remote.dto.LoginRequestDto
import com.example.hotelapp.data.remote.dto.RegisterRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HotelApi {

    @POST("register")
    suspend fun register(@Body request: RegisterRequestDto): AuthResponseDto

    @POST("login")
    suspend fun login(@Body request: LoginRequestDto): AuthResponseDto

    @GET("hotels")
    suspend fun getHotels(
        @Query("city") city: String? = null,
        @Query("maxPrice") maxPrice: Double? = null
    ): List<HotelDto>

    @GET("hotels/{id}")
    suspend fun getHotelById(@Path("id") id: Int): HotelDto

    @POST("bookings")
    suspend fun createBooking(
        @Header("Authorization") token: String,
        @Body request: CreateBookingRequestDto
    ): BookingDto

    @GET("bookings/me")
    suspend fun getMyBookings(
        @Header("Authorization") token: String
    ): List<BookingDto>
}