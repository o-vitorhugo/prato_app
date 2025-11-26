package com.example.appprato.domain.repository

import kotlinx.coroutines.flow.Flow

interface SubstitutionsRepository {
    fun getSubstitution(ingredient: String): Flow<String?>
}
