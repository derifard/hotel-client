package com.example.hotelapp.presentation.hotels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotelapp.domain.model.Hotel
import com.example.hotelapp.domain.usecase.CreateBookingUseCase
import com.example.hotelapp.domain.usecase.GetHotelByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HotelDetailState(
    val isLoading: Boolean = false,
    val hotel: Hotel? = null,
    val isBookingSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HotelDetailViewModel @Inject constructor(
    private val getHotelByIdUseCase: GetHotelByIdUseCase,
    private val createBookingUseCase: CreateBookingUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HotelDetailState())
    val state: StateFlow<HotelDetailState> = _state

    fun loadHotel(id: Int) {
        viewModelScope.launch {
            _state.value = HotelDetailState(isLoading = true)
            try {
                val hotel = getHotelByIdUseCase(id)
                _state.value = HotelDetailState(hotel = hotel)
            } catch (e: Exception) {
                _state.value = HotelDetailState(error = e.message ?: "Ошибка загрузки")
            }
        }
    }

    fun createBooking(hotelId: Int, checkIn: String, checkOut: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                createBookingUseCase(hotelId, checkIn, checkOut)
                _state.value = _state.value.copy(isLoading = false, isBookingSuccess = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ошибка бронирования"
                )
            }
        }
    }
}