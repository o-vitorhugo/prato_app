package com.example.appprato.ui.minhasreceitas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appprato.data.local.RecipeEntity
import com.example.appprato.domain.repository.MainRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MinhasReceitasViewModel @Inject constructor(
    private val repository: MainRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    val userRecipes: StateFlow<List<RecipeEntity>> = repository.getAllRecipes()
        .map { recipes ->
            val userId = auth.currentUser?.uid
            if (userId != null) {
                recipes.filter { it.userId == userId }
            } else {
                emptyList()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insertRecipe(
        name: String,
        description: String,
        prepTime: String,
        ingredients: String,
        instructions: String
    ) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                val newRecipe = RecipeEntity(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    description = description,
                    category = "", // A categoria não está no formulário, então fica vazia
                    imageUrl = "", // A imagem ainda não é tratada
                    ingredients = ingredients.lines(),
                    preparationSteps = instructions.lines(),
                    tags = emptyList(), // As tags não estão no formulário
                    isFavorite = false,
                    userId = userId
                )
                repository.insertRecipe(newRecipe)
            }
        }
    }
}
