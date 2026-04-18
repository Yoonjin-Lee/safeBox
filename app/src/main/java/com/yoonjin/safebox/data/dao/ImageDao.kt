package com.yoonjin.safebox.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.yoonjin.safebox.data.dto.ImageDto
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Insert
    fun insert(imageDto: ImageDto)

    @Delete
    fun delete(imageDto: ImageDto)

    @Query("DELETE FROM imagedto WHERE name = :name")
    fun deleteByName(name: String)

    @Query("SELECT * FROM imagedto")
    fun getAllFlow() : Flow<List<ImageDto>>
}