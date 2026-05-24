package com.example.hotelapp

import com.example.hotelapp.domain.model.AuthResult
import com.example.hotelapp.domain.model.User
import com.example.hotelapp.domain.repository.AuthRepository
import com.example.hotelapp.domain.usecase.LoginUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class LoginUseCaseTest {

    private lateinit var repository: AuthRepository
    private lateinit var useCase: LoginUseCase

    private val fakeUser = User(1, "test@gmail.com", "Тест")
    private val fakeAuthResult = AuthResult("fake_token_123", fakeUser)

    @Before
    fun setup() {
        repository = mock(AuthRepository::class.java)
        useCase = LoginUseCase(repository)
    }

    @Test
    fun `успешный вход возвращает токен`() = runTest {
        `when`(repository.login("test@gmail.com", "password123")).thenReturn(fakeAuthResult)
        val result = useCase("test@gmail.com", "password123")
        assertEquals("fake_token_123", result.token)
    }

    @Test
    fun `успешный вход возвращает пользователя`() = runTest {
        `when`(repository.login("test@gmail.com", "password123")).thenReturn(fakeAuthResult)
        val result = useCase("test@gmail.com", "password123")
        assertEquals("test@gmail.com", result.user.email)
        assertEquals("Тест", result.user.name)
    }

    @Test(expected = Exception::class)
    fun `неверные данные вызывают исключение`() = runTest {
        `when`(repository.login("wrong@gmail.com", "wrongpass"))
            .thenThrow(Exception("Неверный email или пароль"))
        useCase("wrong@gmail.com", "wrongpass")
    }
}