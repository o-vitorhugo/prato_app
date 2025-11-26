package com.example.appprato.navigation

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

// Enum para representar os três estados possíveis de autenticação
enum class AuthState {
    LOADING,      // Verificando se o usuário está logado
    LOGGED_IN,    // Usuário está autenticado
    LOGGED_OUT    // Usuário não está autenticado
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState.LOADING)
    val authState = _authState.asStateFlow()

    // Ouve as mudanças no estado de autenticação do Firebase
    private val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        _authState.value = if (firebaseAuth.currentUser != null) {
            AuthState.LOGGED_IN
        } else {
            AuthState.LOGGED_OUT
        }
    }

    init {
        // Registra o ouvinte quando o ViewModel é criado
        auth.addAuthStateListener(authListener)
    }

    override fun onCleared() {
        // Remove o ouvinte quando o ViewModel é destruído para evitar memory leaks
        super.onCleared()
        auth.removeAuthStateListener(authListener)
    }
}
