package com.yoonjin.safebox.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yoonjin.safebox.data.dao.ImageDao
import com.yoonjin.safebox.data.dto.ImageDto

@Database(entities = [ImageDto::class], version = 7)
abstract class Database : RoomDatabase(){
    abstract fun imageDao() : ImageDao
}