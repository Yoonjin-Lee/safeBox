package com.example.safebox.data

import com.example.safebox.data.repositoryImpl.BitmapRepositoryImpl
import com.example.safebox.domain.repository.BitmapRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindBitmapRepository(
        impl: BitmapRepositoryImpl
    ): BitmapRepository
}