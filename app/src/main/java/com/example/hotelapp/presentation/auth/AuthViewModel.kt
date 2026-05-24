package com.example.hotelapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotelapp.domain.usecase.LoginUseCase
import com.example.hotelapp.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState(isLoading = true)
            try {
                loginUseCase(email, password)
                _state.value = AuthState(isSuccess = true)
            } catch (e: Exception) {
                _state.value = AuthState(error = e.message ?: "Ошибка входа")
            }
        }
    }

    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            _state.value = AuthState(isLoading = true)
            try {
                registerUseCase(email, password, name)
                _state.value = AuthState(isSuccess = true)
            } catch (e: Exception) {
                _state.value = AuthState(error = e.message ?: "Ошибка регистрации")
            }
        }
    }
}