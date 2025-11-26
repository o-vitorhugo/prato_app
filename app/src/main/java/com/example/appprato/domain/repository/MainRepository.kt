package com.example.appprato.domain.repository

import com.example.appprato.data.local.RecipeEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interface do repositório (zerada).
 * A lógica será reconstruída passo a passo.
 */
interface MainRepository {

    suspend fun refreshRecipes()

    fun getAllRecipes(): Flow<List<RecipeEntity>>

    fun getRecipeById(recipeId: String): Flow<RecipeEntity?>

    fun getFavoriteRecipes(): Flow<List<RecipeEntity>>

    suspend fun insertRecipe(recipe: RecipeEntity)

    suspend fun updateRecipe(recipe: RecipeEntity)

    suspend fun deleteRecipe(recipeId: String)

    suspend fun toggleFavoriteStatus(recipeId: String)
}
