package com.example.hotelapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.hotelapp.presentation.auth.LoginScreen
import com.example.hotelapp.ui.theme.HotelAppTheme
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_displaysEmailAndPasswordFields() {
        composeTestRule.setContent {
            HotelAppTheme {
                LoginScreen(
                    onLoginSuccess = {},
                    onNavigateToRegister = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Пароль").assertIsDisplayed()
        composeTestRule.onNodeWithText("Войти").assertIsDisplayed()
    }

    @Test
    fun loginScreen_displaysRegisterButton() {
        composeTestRule.setContent {
            HotelAppTheme {
                LoginScreen(
                    onLoginSuccess = {},
                    onNavigateToRegister = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Нет аккаунта? Зарегистрироваться").assertIsDisplayed()
    }

    @Test
    fun loginScreen_showsValidationError_whenEmailIsInvalid() {
        composeTestRule.setContent {
            HotelAppTheme {
                LoginScreen(
                    onLoginSuccess = {},
                    onNavigateToRegister = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Email").performTextInput("invalidemail")
        composeTestRule.onNodeWithText("Войти").performClick()
        composeTestRule.onNodeWithText("Введите корректный email").assertIsDisplayed()
    }
}