package com.example.appprato.data.repository

import com.example.appprato.domain.repository.SubstitutionsRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SubstitutionsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : SubstitutionsRepository {

    override fun getSubstitution(ingredient: String): Flow<String?> = flow {
        val snapshot = firestore.collection("substitutions").document(ingredient).get().await()
        emit(snapshot.getString("suggestion"))
    }
}
