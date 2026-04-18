package com.yoonjin.safebox.data.module

import com.yoonjin.safebox.data.repositoryImpl.BitmapRepositoryImpl
import com.yoonjin.safebox.domain.repository.BitmapRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindBitmapRepository(
        impl: BitmapRepositoryImpl
    ): BitmapRepository
}