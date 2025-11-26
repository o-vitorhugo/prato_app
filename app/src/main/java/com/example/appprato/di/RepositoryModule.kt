package com.example.appprato.di

import com.example.appprato.data.repository.AuthRepositoryImpl
import com.example.appprato.data.repository.MainRepositoryImpl
import com.example.appprato.data.repository.NotificationsRepositoryImpl
import com.example.appprato.data.repository.SubstitutionsRepositoryImpl
import com.example.appprato.data.repository.UserRepositoryImpl
import com.example.appprato.domain.repository.AuthRepository
import com.example.appprato.domain.repository.MainRepository
import com.example.appprato.domain.repository.NotificationsRepository
import com.example.appprato.domain.repository.SubstitutionsRepository
import com.example.appprato.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindMainRepository(
        mainRepositoryImpl: MainRepositoryImpl
    ): MainRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindNotificationsRepository(
        notificationsRepositoryImpl: NotificationsRepositoryImpl
    ): NotificationsRepository

    @Binds
    @Singleton
    abstract fun bindSubstitutionsRepository(
        substitutionsRepositoryImpl: SubstitutionsRepositoryImpl
    ): SubstitutionsRepository
}
