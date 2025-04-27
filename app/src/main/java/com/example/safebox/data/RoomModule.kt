package com.example.safebox.data

import android.content.Context
import androidx.room.Room
import com.example.safebox.data.dao.ImageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): Database = Room
        .databaseBuilder(context, Database::class.java, "safe_box.db")
        .build()

    @Singleton
    @Provides
    fun provideImageDao(database: Database): ImageDao = database.imageDao()
}