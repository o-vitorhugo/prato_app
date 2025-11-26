package com.example.appprato.model

import com.example.appprato.data.local.RecipeEntity

data class Receita(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val imageUrl: String = "",
    val ingredients: List<String> = emptyList(),
    val preparationSteps: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val isFavorite: Boolean = false,
    val userId: String = ""
)

fun Receita.toEntity(): RecipeEntity {
    return RecipeEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        category = this.category,
        imageUrl = this.imageUrl,
        ingredients = this.ingredients,
        preparationSteps = this.preparationSteps,
        tags = this.tags,
        isFavorite = this.isFavorite,
        userId = this.userId
    )
}

fun RecipeEntity.toReceita(): Receita {
    return Receita(
        id = this.id,
        name = this.name,
        description = this.description,
        category = this.category,
        imageUrl = this.imageUrl,
        ingredients = this.ingredients,
        preparationSteps = this.preparationSteps,
        tags = this.tags,
        isFavorite = this.isFavorite,
        userId = this.userId
    )
}
