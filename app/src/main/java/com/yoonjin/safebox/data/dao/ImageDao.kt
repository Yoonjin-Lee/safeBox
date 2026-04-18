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

    @Query("DELETE FROM imagedto WHERE name = :name")
    suspend fun deleteByName(name: String)

    @Query("SELECT uuid, name, format FROM imagedto")
    fun getAllFlow(): Flow<List<ImageListDto>>

    @Query("SELECT bitmap FROM imagedto WHERE name = :name")
    suspend fun getImageByName(name: String): List<ByteArray>
}