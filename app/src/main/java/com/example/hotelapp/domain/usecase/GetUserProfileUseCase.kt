package com.example.hotelapp.domain.usecase

import com.example.hotelapp.domain.repository.AuthRepository
import javax.inject.Inject

data class UserProfile(
    val name: String,
    val email: String,
    val phone: String
)

class GetUserProfileUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): UserProfile {
        return UserProfile(
            name = repository.getUserName(),
            email = repository.getUserEmail(),
            phone = repository.getUserPhone()
        )
    }
}