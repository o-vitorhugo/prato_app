package com.example.appprato.ui.detalhes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appprato.data.local.RecipeEntity
import com.example.appprato.domain.repository.MainRepository
import com.example.appprato.domain.repository.SubstitutionsRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Estado da UI para a tela de detalhes
data class DetalhesUiState(
    val isLoading: Boolean = false,
    val recipe: RecipeEntity? = null,
    val isOwnedByUser: Boolean = false, // Novo estado para controlar a visibilidade do botão
    val substitutionSuggestion: String? = null,
    val error: String? = null
)

@HiltViewModel
class DetalhesViewModel @Inject constructor(
    private val repository: MainRepository,
    private val substitutionsRepository: SubstitutionsRepository,
    private val auth: FirebaseAuth, // Injetando o FirebaseAuth
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalhesUiState())
    val uiState = _uiState.asStateFlow()

    private val recipeId: String = checkNotNull(savedStateHandle["recipeId"])

    init {
        loadRecipeDetails()
    }

    private fun loadRecipeDetails() {
        viewModelScope.launch {
            repository.getRecipeById(recipeId)
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                .collect { recipe ->
                    val currentUserId = auth.currentUser?.uid
                    _uiState.update { it.copy(
                        isLoading = false, 
                        recipe = recipe,
                        isOwnedByUser = recipe?.userId == currentUserId
                    ) }
                }
        }
    }

    fun onIngredientClicked(ingredient: String) {
        viewModelScope.launch {
            substitutionsRepository.getSubstitution(ingredient).collect { suggestion ->
                _uiState.update { it.copy(substitutionSuggestion = suggestion) }
            }
        }
    }

    fun clearSuggestion() {
        _uiState.update { it.copy(substitutionSuggestion = null) }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            try {
                repository.toggleFavoriteStatus(recipeId)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Erro ao favoritar: ${e.message}") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
