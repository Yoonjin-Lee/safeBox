package com.example.safebox.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.safebox.data.dao.ImageDao
import com.example.safebox.data.dto.ImageDto

@Database(entities = [ImageDto::class], version = 7)
abstract class Database : RoomDatabase(){
    abstract fun imageDao() : ImageDao
}