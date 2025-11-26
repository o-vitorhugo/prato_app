package com.example.appprato.data.repository

import android.net.Uri
import com.example.appprato.data.model.User
import com.example.appprato.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    private val defaultPhotoUrl = "https://static.vecteezy.com/system/resources/previews/052/793/073/non_2x/chef-logo-design-vector.jpg"

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun signIn(email: String, password: String): Result<Unit> = runCatching {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signUp(name: String, email: String, password: String): Result<Unit> = runCatching {
        val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val user = authResult.user ?: throw IllegalStateException("User not created")

        // Atualiza o perfil de autenticação do Firebase
        val profileUpdates = userProfileChangeRequest {
            displayName = name
            photoUri = Uri.parse(defaultPhotoUrl)
        }
        user.updateProfile(profileUpdates).await()

        // Cria o documento do usuário no Firestore
        val newUser = User(
            uid = user.uid,
            name = name,
            email = email,
            photoUrl = defaultPhotoUrl
        )
        firestore.collection("users").document(user.uid).set(newUser).await()
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}
