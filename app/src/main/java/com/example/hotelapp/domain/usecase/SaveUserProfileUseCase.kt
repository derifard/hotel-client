package com.example.hotelapp.domain.usecase

import com.example.hotelapp.domain.repository.AuthRepository
import javax.inject.Inject

class SaveUserProfileUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, phone: String) {
        repository.saveUserProfile(name, email, phone)
    }
}