package com.example.appprato.data.remote

import com.example.appprato.data.RecipeRemoteDataSource
import com.example.appprato.data.local.RecipeEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RecipeRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : RecipeRemoteDataSource {

    override suspend fun getAllRecipes(): List<RecipeEntity> {
        return try {
            firestore.collection("recipes").get().await().toObjects(RecipeEntity::class.java)
        } catch (e: Exception) {
            // Tratar exceções de rede ou do Firestore
            emptyList()
        }
    }

    override suspend fun uploadRecipe(recipe: RecipeEntity) {
        firestore.collection("recipes").document(recipe.id).set(recipe).await()
    }
}
