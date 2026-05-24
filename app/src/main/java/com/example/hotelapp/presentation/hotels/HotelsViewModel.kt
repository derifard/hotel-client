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

data class HotelsState(
    val isLoading: Boolean = false,
    val hotels: List<Hotel> = emptyList(),
    val error: String? = null
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
            _state.value = HotelsState(isLoading = true)
            try {
                val hotels = getHotelsUseCase(city, maxPrice)
                _state.value = HotelsState(hotels = hotels)
            } catch (e: Exception) {
                _state.value = HotelsState(error = e.message ?: "Ошибка загрузки")
            }
        }
    }
}