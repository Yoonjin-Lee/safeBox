package com.yoonjin.safebox.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yoonjin.safebox.data.dao.ImageDao
import com.yoonjin.safebox.data.dto.ImageDto

@Database(
    entities = [ImageDto::class],
    version = 8,
    exportSchema = true
)
abstract class Database : RoomDatabase(){
    abstract fun imageDao() : ImageDao
}


val MIGRATION_7_8 = Migration(7, 8){ db ->
    db.execSQL(
        """
            ALTER TABLE ImageDto
            ADD COLUMN groupName TEXT NOT NULL DEFAULT ''
        """.trimIndent()
    )
}