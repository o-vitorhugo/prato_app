package com.example.appprato.ui.perfil

import android.net.Uri

data class PerfilUiState(
    val userName: String? = null,
    val userEmail: String? = null,
    val userPhotoUrl: Uri? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
