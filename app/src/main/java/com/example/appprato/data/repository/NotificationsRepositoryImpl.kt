package com.example.appprato.data.repository

import com.example.appprato.data.model.Notification
import com.example.appprato.domain.repository.NotificationsRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotificationsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : NotificationsRepository {

    override fun getNotifications(): Flow<List<Notification>> {
        return firestore.collection("notifications")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(Notification::class.java)
            }
    }

    override suspend fun createNotification(notification: Notification) {
        firestore.collection("notifications").document(notification.id).set(notification)
    }
}
