package com.example.appprato.domain.repository

import com.example.appprato.data.model.Notification
import kotlinx.coroutines.flow.Flow

// Interface para o repositório de notificações
interface NotificationsRepository {
    fun getNotifications(): Flow<List<Notification>>
    suspend fun createNotification(notification: Notification) // <-- Adicionado
}
