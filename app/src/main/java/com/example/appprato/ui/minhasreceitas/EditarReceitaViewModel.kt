package com.example.appprato.ui.minhasreceitas

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appprato.data.local.RecipeEntity
import com.example.appprato.domain.repository.MainRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class EditarReceitaUiState(
    val isLoading: Boolean = false,
    val recipe: RecipeEntity? = null,
    val isNewRecipe: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null,
    val isSaveEnabled: Boolean = false
)

@HiltViewModel
class EditarReceitaViewModel @Inject constructor(
    private val repository: MainRepository,
    private val auth: FirebaseAuth,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditarReceitaUiState())
    val uiState = _uiState.asStateFlow()

    private val recipeId: String? = savedStateHandle["recipeId"]

    init {
        if (recipeId == null) {
            val userId = auth.currentUser?.uid
            if (userId.isNullOrEmpty()) {
                _uiState.update { it.copy(error = "Não foi possível identificar o usuário. Tente novamente.", saveSuccess = true) }
            } else {
                _uiState.update { it.copy(
                    isNewRecipe = true,
                    recipe = RecipeEntity(
                        id = UUID.randomUUID().toString(),
                        name = "",
                        description = "",
                        category = "",
                        imageUrl = "",
                        ingredients = emptyList(),
                        preparationSteps = emptyList(),
                        tags = emptyList(),
                        userId = userId
                    )
                ) }
            }
        } else {
            loadRecipe(recipeId)
        }
    }

    private fun loadRecipe(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.getRecipeById(id).collect { recipe ->
                    if (recipe?.userId != auth.currentUser?.uid) {
                        _uiState.update { it.copy(error = "Você não tem permissão para editar esta receita.", saveSuccess = true) }
                        return@collect
                    }
                    _uiState.update { it.copy(isLoading = false, recipe = recipe, isSaveEnabled = validateRecipe(recipe)) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun validateRecipe(recipe: RecipeEntity?): Boolean {
        if (recipe == null) return false
        return recipe.name.isNotBlank() &&
                recipe.description.isNotBlank() &&
                recipe.category.isNotBlank() &&
                recipe.imageUrl.isNotBlank() &&
                recipe.ingredients.isNotEmpty() &&
                recipe.preparationSteps.isNotEmpty()
    }

    private fun updateRecipe(updateAction: (RecipeEntity) -> RecipeEntity) {
        _uiState.update { currentState ->
            val updatedRecipe = currentState.recipe?.let(updateAction)
            currentState.copy(
                recipe = updatedRecipe,
                isSaveEnabled = validateRecipe(updatedRecipe)
            )
        }
    }

    fun onNameChange(name: String) = updateRecipe { it.copy(name = name) }
    fun onDescriptionChange(description: String) = updateRecipe { it.copy(description = description) }
    fun onCategoryChange(category: String) = updateRecipe { it.copy(category = category) }
    fun onImageUrlChange(imageUrl: String) = updateRecipe { it.copy(imageUrl = imageUrl) }
    fun onIngredientsChange(ingredients: String) = updateRecipe { it.copy(ingredients = ingredients.lines().filter { it.isNotBlank() }) }
    fun onStepsChange(steps: String) = updateRecipe { it.copy(preparationSteps = steps.lines().filter { it.isNotBlank() }) }

    fun onTagToggled(tag: String) {
        updateRecipe { recipe ->
            val newTags = if (tag in recipe.tags) recipe.tags - tag else recipe.tags + tag
            recipe.copy(tags = newTags)
        }
    }

    fun saveRecipe() {
        if (!_uiState.value.isSaveEnabled) {
            _uiState.update { it.copy(error = "Por favor, preencha todos os campos obrigatórios.") }
            return
        }

        viewModelScope.launch {
            val recipeToSave = _uiState.value.recipe
            if (recipeToSave == null || recipeToSave.userId.isEmpty()) {
                _uiState.update { it.copy(error = "Você precisa estar logado para salvar.") }
                return@launch
            }

            try {
                if (_uiState.value.isNewRecipe) {
                    repository.insertRecipe(recipeToSave)
                } else {
                    repository.updateRecipe(recipeToSave)
                }
                _uiState.update { it.copy(saveSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
