package com.example.travelagencyandroid.viewmodel

import androidx.lifecycle.ViewModel
import com.example.travelagencyandroid.Repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String = "Success") : AuthState()
    data class Error(val error: String) : AuthState()
}

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registerState: StateFlow<AuthState> = _registerState

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> = _loginState

    fun registerUser(email: String, password: String, firstName: String, lastName: String, country: String) {
        _registerState.value = AuthState.Loading
        repository.registerUser(email, password, firstName, lastName, country) { success, error ->
            if (success) {
                _registerState.value = AuthState.Success("Registration successful")
            } else {
                _registerState.value = AuthState.Error(error ?: "Unknown error")
            }
        }
    }

    fun loginUser(email: String, password: String) {
        _loginState.value = AuthState.Loading
        repository.loginUser(email, password) { success, error ->
            if (success) {
                _loginState.value = AuthState.Success("Login successful")
            } else {
                _loginState.value = AuthState.Error(error ?: "Unknown error")
            }
        }
    }
}
