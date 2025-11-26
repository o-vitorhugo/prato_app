package com.example.appprato.ui.perfil

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appprato.data.model.User
import com.example.appprato.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class GerenciarPerfilUiState(
    val name: String = "",
    val email: String = "",
    val photoUri: Uri? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class GerenciarPerfilViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(GerenciarPerfilUiState())
    val uiState = _uiState.asStateFlow()

    private var currentUser: User? = null

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        val firebaseUser = auth.currentUser
        if (firebaseUser == null) {
            _uiState.update { it.copy(error = "Usuário não encontrado. Faça o login novamente.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                userRepository.getUser(firebaseUser.uid)
                    .collect { userProfile ->
                        currentUser = userProfile
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                name = userProfile?.name ?: firebaseUser.displayName ?: "",
                                email = userProfile?.email ?: firebaseUser.email ?: "",
                                photoUri = (userProfile?.photoUrl?.let { Uri.parse(it) } ?: firebaseUser.photoUrl),
                                error = null
                            )
                        }
                    }
            } catch (t: Throwable) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        name = firebaseUser.displayName ?: "",
                        email = firebaseUser.email ?: "",
                        photoUri = firebaseUser.photoUrl,
                        error = "Não foi possível carregar os dados do perfil."
                    )
                }
            }
        }
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun saveProfile(onSaveSuccess: () -> Unit) {
        viewModelScope.launch {
            val firebaseUser = auth.currentUser ?: return@launch
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val currentName = _uiState.value.name

                // 1. Atualiza o perfil de autenticação do Firebase (apenas o nome)
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(currentName)
                    .build()
                firebaseUser.updateProfile(profileUpdates).await()

                // 2. Atualiza os dados do usuário no Firestore (apenas o nome)
                currentUser?.let {
                    val updatedUser = it.copy(name = currentName)
                    userRepository.updateUser(updatedUser)
                }

                _uiState.update { it.copy(isLoading = false) }
                onSaveSuccess()

            } catch (t: Throwable) {
                _uiState.update { it.copy(isLoading = false, error = "Erro ao salvar o perfil: ${t.message}") }
            }
        }
    }
}
