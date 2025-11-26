package com.example.appprato.data

import com.example.appprato.data.local.RecipeEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interfaces das fontes de dados.
 */
interface RecipeLocalDataSource {
    fun getAllRecipes(): Flow<List<RecipeEntity>>
    fun getRecipeById(recipeId: String): Flow<RecipeEntity?>
    fun getFavoriteRecipes(): Flow<List<RecipeEntity>>
    suspend fun insertRecipe(recipe: RecipeEntity)
    suspend fun insertAll(recipes: List<RecipeEntity>)
    suspend fun updateRecipe(recipe: RecipeEntity)
}

interface RecipeRemoteDataSource {
    suspend fun getAllRecipes(): List<RecipeEntity>
    suspend fun uploadRecipe(recipe: RecipeEntity)
}
