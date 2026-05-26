package com.example.hotelapp.domain.usecase

import com.example.hotelapp.domain.repository.AuthRepository
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): String? {
        return repository.getToken()
    }
}