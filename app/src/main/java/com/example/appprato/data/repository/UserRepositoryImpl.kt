package com.example.appprato.data.repository

import com.example.appprato.data.model.User
import com.example.appprato.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    override fun getUser(uid: String): Flow<User?> {
        return firestore.collection("users").document(uid)
            .snapshots() // Retorna um Flow que emite a cada mudança no documento
            .map { snapshot ->
                snapshot.toObject(User::class.java) // Converte o snapshot para o objeto User
            }
    }

    override suspend fun createUser(user: User) {
        firestore.collection("users").document(user.uid).set(user).await()
    }

    override suspend fun updateUser(user: User) {
        firestore.collection("users").document(user.uid).set(user).await()
    }
}
