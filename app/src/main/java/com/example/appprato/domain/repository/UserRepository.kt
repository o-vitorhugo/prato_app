package com.example.appprato.domain.repository

import com.example.appprato.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(uid: String): Flow<User?>
    suspend fun createUser(user: User)
    suspend fun updateUser(user: User)
}
