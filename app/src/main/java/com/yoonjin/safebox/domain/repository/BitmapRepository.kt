package com.yoonjin.safebox.domain.repository

import com.yoonjin.safebox.domain.entity.ImageEntity
import com.yoonjin.safebox.domain.entity.ImageListEntity
import kotlinx.coroutines.flow.Flow

interface BitmapRepository {
    suspend fun saveBitmap(bitmap: ByteArray, name: String)
    suspend fun getBitmapList(): Flow<List<ImageListEntity>>
    suspend fun deleteImageGroup(name: String)
    suspend fun getBitmapArray(name: String): List<ByteArray>
}