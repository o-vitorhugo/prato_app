package com.example.appprato.data.model

import com.google.firebase.Timestamp

// Representa um único documento na coleção 'notifications' do Firestore.
data class Notification(
    val id: String = "",
    val message: String = "",
    val recipeId: String? = null, // ID da receita para navegação
    val userId: String = "",      // ID do usuário que gerou a notificação
    val timestamp: Timestamp = Timestamp.now()
) {
    // Construtor vazio necessário para o Firestore
    constructor() : this("", "", null, "", Timestamp.now())
}
