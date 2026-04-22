package com.yoonjin.safebox.domain.repository

import com.yoonjin.safebox.domain.entity.ImageEntity
import com.yoonjin.safebox.domain.entity.ImageListEntity
import kotlinx.coroutines.flow.Flow

interface BitmapRepository {
    suspend fun saveBitmap(bitmap: ByteArray, name: String, groupName: String)
    suspend fun getBitmapList(): Flow<List<ImageListEntity>>
    suspend fun deleteImageGroup(name: String)
    suspend fun getBitmapArrayByGroupName(name: String): List<ByteArray>
    suspend fun getGroupName(): String
    suspend fun setGroupName(groupName: String)
}