package com.yoonjin.safebox.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.yoonjin.safebox.data.dto.ImageDto
import com.yoonjin.safebox.data.dto.ImageListDto
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Insert
    suspend fun insert(imageDto: ImageDto)

    @Delete
    suspend fun delete(imageDto: ImageDto)

    @Query("DELETE FROM imagedto WHERE groupName = :name")
    suspend fun deleteByGroupName(name: String)

    @Query("SELECT uuid, name, format, groupName FROM imagedto")
    fun getAllFlow(): Flow<List<ImageListDto>>

    @Query("SELECT bitmap FROM imagedto WHERE groupName = :name")
    suspend fun getImageByGroupName(name: String): List<ByteArray>

    @Query("SELECT COUNT(*) FROM imagedto")
    suspend fun getNumberOfList(): Int
}