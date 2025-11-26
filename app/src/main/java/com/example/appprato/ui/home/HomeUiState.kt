package com.example.appprato.ui.home

import com.example.appprato.data.local.RecipeEntity

data class HomeUiState(
    val isLoading: Boolean = false,
    val allRecipes: List<RecipeEntity> = emptyList(),
    val filteredRecipes: List<RecipeEntity> = emptyList(),
    val searchQuery: String = "",
    val categories: List<String> = emptyList(),
    val selectedCategory: String? = null,
    val availableTags: List<String> = emptyList(),
    val selectedTags: Set<String> = emptySet(),
    val error: String? = null
)
