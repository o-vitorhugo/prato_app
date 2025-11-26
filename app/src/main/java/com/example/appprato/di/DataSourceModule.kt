package com.example.appprato.di

import com.example.appprato.data.RecipeLocalDataSource
import com.example.appprato.data.local.RecipeLocalDataSourceImpl
import com.example.appprato.data.RecipeRemoteDataSource
import com.example.appprato.data.remote.RecipeRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindRecipeLocalDataSource(
        recipeLocalDataSourceImpl: RecipeLocalDataSourceImpl
    ): RecipeLocalDataSource

    @Binds
    @Singleton
    abstract fun bindRecipeRemoteDataSource(
        recipeRemoteDataSourceImpl: RecipeRemoteDataSourceImpl
    ): RecipeRemoteDataSource
}
