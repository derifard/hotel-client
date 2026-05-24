package com.example.hotelapp.domain.usecase

import com.example.hotelapp.domain.model.AuthResult
import com.example.hotelapp.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, name: String): AuthResult {
        return repository.register(email, password, name)
    }
}