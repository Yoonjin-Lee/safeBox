package com.yoonjin.safebox.data.module

import android.content.Context
import androidx.room.Room
import com.yoonjin.safebox.data.Database
import com.yoonjin.safebox.data.MIGRATION_7_8
import com.yoonjin.safebox.data.dao.ImageDao
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
        .addMigrations(MIGRATION_7_8)
        .build()

    @Singleton
    @Provides
    fun provideImageDao(database: Database): ImageDao = database.imageDao()
}