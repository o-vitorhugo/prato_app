package com.example.appprato.ui.notifications

import com.example.appprato.data.model.Notification

// Agora, o estado contém apenas uma lista unificada de notificações.
data class NotificationsUiState(
    val isLoading: Boolean = false,
    val notifications: List<Notification> = emptyList(),
    val error: String? = null
)
