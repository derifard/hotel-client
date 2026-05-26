package com.example.hotelapp.presentation.hotels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotelapp.domain.model.Hotel
import com.example.hotelapp.domain.usecase.GetHotelsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SortOrder {
    NONE, PRICE_ASC, PRICE_DESC, RATING_DESC
}

data class HotelsState(
    val isLoading: Boolean = false,
    val hotels: List<Hotel> = emptyList(),
    val error: String? = null,
    val sortOrder: SortOrder = SortOrder.NONE
)

@HiltViewModel
class HotelsViewModel @Inject constructor(
    private val getHotelsUseCase: GetHotelsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HotelsState())
    val state: StateFlow<HotelsState> = _state

    init {
        loadHotels()
    }

    fun loadHotels(city: String? = null, maxPrice: Double? = null) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val hotels = getHotelsUseCase(city, maxPrice)
                _state.value = _state.value.copy(
                    isLoading = false,
                    hotels = sortHotels(hotels, _state.value.sortOrder)
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ошибка загрузки"
                )
            }
        }
    }

    fun setSortOrder(order: SortOrder) {
        val sorted = sortHotels(_state.value.hotels, order)
        _state.value = _state.value.copy(sortOrder = order, hotels = sorted)
    }

    private fun sortHotels(hotels: List<Hotel>, order: SortOrder): List<Hotel> {
        return when (order) {
            SortOrder.PRICE_ASC -> hotels.sortedBy { it.pricePerNight }
            SortOrder.PRICE_DESC -> hotels.sortedByDescending { it.pricePerNight }
            SortOrder.RATING_DESC -> hotels.sortedByDescending { it.rating }
            SortOrder.NONE -> hotels
        }
    }
}