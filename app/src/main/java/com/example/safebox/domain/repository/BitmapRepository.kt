package com.example.safebox.domain.repository

import android.graphics.Bitmap
import com.example.safebox.domain.entity.ImageEntity
import kotlinx.coroutines.flow.Flow

interface BitmapRepository {
    suspend fun saveBitmap(bitmap: ByteArray)
    suspend fun getBitmapList(): Flow<List<ImageEntity>>

    suspend fun increaseImageCounter()
}