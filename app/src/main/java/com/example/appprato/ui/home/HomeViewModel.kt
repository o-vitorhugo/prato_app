package com.example.appprato.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appprato.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            repository.refreshRecipes()

            repository.getAllRecipes()
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                .collect { recipes ->
                    val categories = listOf("Todos") + recipes.map { it.category }.distinct().sorted()
                    val tags = listOf("Sem Glúten", "Sem Lactose", "Vegano", "Vegetariano", "Sobremesa", "Rápida")
                    _uiState.update { it.copy(
                        isLoading = false,
                        allRecipes = recipes,
                        categories = categories,
                        availableTags = tags
                    )}
                    filterRecipes()
                }
        }
    }

    private fun filterRecipes() {
        _uiState.update { state ->
            val filtered = state.allRecipes.filter { recipe ->
                val matchesSearch = recipe.name.contains(state.searchQuery, ignoreCase = true) ||
                        recipe.description.contains(state.searchQuery, ignoreCase = true)
                val matchesCategory = state.selectedCategory == null || state.selectedCategory == "Todos" || recipe.category == state.selectedCategory
                val matchesTags = state.selectedTags.isEmpty() || recipe.tags.containsAll(state.selectedTags)

                matchesSearch && matchesCategory && matchesTags
            }
            state.copy(filteredRecipes = filtered)
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        filterRecipes()
    }

    fun onCategorySelected(category: String) {
        _uiState.update { it.copy(selectedCategory = if (category == "Todos") null else category) }
        filterRecipes()
    }

    fun onTagSelected(tag: String) {
        _uiState.update { state ->
            val newTags = if (tag in state.selectedTags) state.selectedTags - tag else state.selectedTags + tag
            state.copy(selectedTags = newTags)
        }
        filterRecipes()
    }

    fun toggleFavorite(recipeId: String) {
        viewModelScope.launch {
            try {
                repository.toggleFavoriteStatus(recipeId)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
