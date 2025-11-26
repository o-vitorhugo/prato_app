package com.example.appprato.ui.login

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun login(email: String, password: String, keepConnected: Boolean) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            if (email.isBlank() || password.isBlank()) {
                _uiState.update { it.copy(isLoading = false, error = "Email e senha devem ser preenchidos.") }
                return@launch
            }
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                if (keepConnected) {
                    sharedPreferences.edit().putBoolean("KEEP_CONNECTED", true).apply()
                } else {
                    sharedPreferences.edit().remove("KEEP_CONNECTED").apply()
                }
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage ?: "Ocorreu um erro de autenticação.") }
            }
        }
    }
}
