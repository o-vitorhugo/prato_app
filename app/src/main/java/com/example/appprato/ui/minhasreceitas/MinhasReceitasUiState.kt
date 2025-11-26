package com.example.appprato.ui.minhasreceitas

import com.example.appprato.data.local.RecipeEntity

// Este estado agora controla apenas o carregamento e os erros.
// A lista de receitas será um fluxo de dados separado e reativo.
data class MinhasReceitasUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)
