package com.example.appprato.ui.perfil

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appprato.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val user = auth.currentUser ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                userRepository.getUser(user.uid)
                    .collect { userProfile ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                userName = userProfile?.name ?: user.displayName,
                                userEmail = userProfile?.email ?: user.email,
                                userPhotoUrl = userProfile?.photoUrl?.let { Uri.parse(it) } ?: user.photoUrl,
                                error = null // Clear error on success
                            )
                        }
                    }
            } catch (e: Throwable) {
                // Fallback to auth data if repository fails
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        userName = user.displayName,
                        userEmail = user.email,
                        userPhotoUrl = user.photoUrl,
                        error = null // Explicitly don't show an error
                    )
                }
            }
        }
    }

    fun logout() {
        auth.signOut()
    }
}
