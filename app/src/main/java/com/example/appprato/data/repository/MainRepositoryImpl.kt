package com.example.appprato.data.repository

import com.example.appprato.data.local.RecipeDao
import com.example.appprato.data.local.RecipeEntity
import com.example.appprato.domain.repository.MainRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao,
    private val firestore: FirebaseFirestore
) : MainRepository {

    override suspend fun refreshRecipes() {
        try {
            val snapshot = firestore.collection("recipes").get().await()
            val recipes = snapshot.toObjects(RecipeEntity::class.java)
            recipeDao.insertAll(recipes)
        } catch (e: Exception) {
            // Tratar o erro, talvez logar ou expor para a UI
        }
    }

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

    override suspend fun updateRecipe(recipe: RecipeEntity) {
        recipeDao.updateRecipe(recipe)
    }

    override suspend fun deleteRecipe(recipeId: String) {
        recipeDao.deleteRecipeById(recipeId)
    }

    override suspend fun toggleFavoriteStatus(recipeId: String) {
        val recipe = recipeDao.getRecipeByIdSuspend(recipeId)
        if (recipe != null) {
            val updatedRecipe = recipe.copy(isFavorite = !recipe.isFavorite)
            recipeDao.updateRecipe(updatedRecipe)
        }
    }
}
