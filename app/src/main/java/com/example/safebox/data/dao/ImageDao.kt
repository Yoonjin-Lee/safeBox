package com.example.safebox.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.safebox.data.dto.ImageDto

@Dao
interface ImageDao {
    @Insert
    fun insert(imageDto: ImageDto)

    @Delete
    fun delete(imageDto: ImageDto)

    @Query("SELECT * FROM imagedto")
    fun getAll() : List<ImageDto>
}