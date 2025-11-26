package com.example.appprato.ui.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appprato.data.local.RecipeEntity
import com.example.appprato.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Data class para o estado da UI
data class FavoritosUiState(
    val isLoading: Boolean = false,
    val favoriteRecipes: List<RecipeEntity> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class FavoritosViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritosUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadFavoriteRecipes()
    }

    private fun loadFavoriteRecipes() {
        viewModelScope.launch {
            repository.getFavoriteRecipes()
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                .collect { recipes ->
                    _uiState.update { it.copy(isLoading = false, favoriteRecipes = recipes) }
                }
        }
    }

    fun toggleFavorite(recipeId: String) {
        viewModelScope.launch {
            try {
                repository.toggleFavoriteStatus(recipeId)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Erro ao desfavoritar: ${e.message}") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
