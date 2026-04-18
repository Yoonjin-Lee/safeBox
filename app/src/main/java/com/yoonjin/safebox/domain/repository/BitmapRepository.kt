package com.yoonjin.safebox.domain.repository

import com.yoonjin.safebox.domain.entity.ImageEntity
import kotlinx.coroutines.flow.Flow

interface BitmapRepository {
    suspend fun saveBitmap(bitmap: ByteArray, name: String)
    suspend fun getBitmapList(): Flow<List<ImageEntity>>
    suspend fun deleteImageGroup(name: String)
}