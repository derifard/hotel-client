package com.example.hotelapp.presentation.bookings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotelapp.domain.model.Booking
import com.example.hotelapp.domain.usecase.DeleteBookingUseCase
import com.example.hotelapp.domain.usecase.GetMyBookingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookingsState(
    val isLoading: Boolean = false,
    val bookings: List<Booking> = emptyList(),
    val error: String? = null,
    val deletingId: Int? = null
)

@HiltViewModel
class BookingsViewModel @Inject constructor(
    private val getMyBookingsUseCase: GetMyBookingsUseCase,
    private val deleteBookingUseCase: DeleteBookingUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BookingsState())
    val state: StateFlow<BookingsState> = _state

    init {
        loadBookings()
    }

    fun loadBookings() {
        viewModelScope.launch {
            _state.value = BookingsState(isLoading = true)
            try {
                val bookings = getMyBookingsUseCase()
                _state.value = BookingsState(bookings = bookings)
            } catch (e: Exception) {
                _state.value = BookingsState(error = e.message ?: "Ошибка загрузки")
            }
        }
    }

    fun deleteBooking(bookingId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(deletingId = bookingId)
            try {
                val success = deleteBookingUseCase(bookingId)
                if (success) {
                    loadBookings()
                } else {
                    _state.value = _state.value.copy(
                        deletingId = null,
                        error = "Не удалось отменить бронирование"
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    deletingId = null,
                    error = e.message ?: "Ошибка отмены"
                )
            }
        }
    }
}