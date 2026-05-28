package com.example.hotelapp.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotelapp.domain.usecase.GetUserProfileUseCase
import com.example.hotelapp.domain.usecase.LogoutUseCase
import com.example.hotelapp.domain.usecase.SaveUserProfileUseCase
import com.example.hotelapp.domain.usecase.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileState(
    val isLoading: Boolean = false,
    val profile: UserProfile = UserProfile("", "", ""),
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val saveUserProfileUseCase: SaveUserProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val profile = getUserProfileUseCase()
                _state.value = _state.value.copy(isLoading = false, profile = profile)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    fun saveProfile(name: String, email: String, phone: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true)
            try {
                saveUserProfileUseCase(name, email, phone)
                _state.value = _state.value.copy(
                    isSaving = false,
                    saveSuccess = true,
                    profile = UserProfile(name, email, phone)
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isSaving = false,
                    error = e.message ?: "Ошибка сохранения"
                )
            }
        }
    }

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            logoutUseCase()
            onComplete()
        }
    }
}