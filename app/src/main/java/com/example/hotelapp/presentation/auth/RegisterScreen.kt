package com.example.hotelapp.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onRegisterSuccess()
    }

    fun validate(): Boolean {
        var valid = true
        if (name.isBlank()) {
            nameError = "Введите имя"
            valid = false
        } else nameError = ""
        if (email.isBlank() || !email.contains("@")) {
            emailError = "Введите корректный email"
            valid = false
        } else emailError = ""
        if (password.length < 6) {
            passwordError = "Пароль должен быть не менее 6 символов"
            valid = false
        } else passwordError = ""
        return valid
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "🏨", fontSize = 48.sp, modifier = Modifier.padding(bottom = 8.dp))
        Text(
            text = "Регистрация",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Создайте новый аккаунт",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it; nameError = "" },
            label = { Text("Имя") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            isError = nameError.isNotEmpty(),
            supportingText = { if (nameError.isNotEmpty()) Text(nameError, color = MaterialTheme.colorScheme.error) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it; emailError = "" },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailError.isNotEmpty(),
            supportingText = { if (emailError.isNotEmpty()) Text(emailError, color = MaterialTheme.colorScheme.error) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it; passwordError = "" },
            label = { Text("Пароль") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = passwordError.isNotEmpty(),
            supportingText = { if (passwordError.isNotEmpty()) Text(passwordError, color = MaterialTheme.colorScheme.error) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state.error != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                Text(
                    text = state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        Button(
            onClick = { if (validate()) viewModel.register(email, password, name) },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Зарегистрироваться", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToLogin) {
            Text("Уже есть аккаунт? Войти")
        }
    }
}