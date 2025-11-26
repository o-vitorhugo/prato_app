package com.example.appprato.data.local

import com.example.appprato.data.RecipeLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecipeLocalDataSourceImpl @Inject constructor(
    private val recipeDao: RecipeDao
) : RecipeLocalDataSource {

    override fun getAllRecipes(): Flow<List<RecipeEntity>> {
        return recipeDao.getAllRecipes()
    }

    override fun getRecipeById(recipeId: String): Flow<RecipeEntity?> {
        return recipeDao.getRecipeById(recipeId)
    }

    override fun getFavoriteRecipes(): Flow<List<RecipeEntity>> {
        return recipeDao.getFavoriteRecipes()
    }

    override suspend fun insertRecipe(recipe: RecipeEntity) {
        recipeDao.insertRecipe(recipe)
    }

    override suspend fun insertAll(recipes: List<RecipeEntity>) {
        recipeDao.insertAll(recipes)
    }

    override suspend fun updateRecipe(recipe: RecipeEntity) {
        recipeDao.updateRecipe(recipe)
    }
}
