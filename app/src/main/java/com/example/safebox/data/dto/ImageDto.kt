package com.example.safebox.data.dto

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageDto(
    @PrimaryKey val uuid: String,
    @ColumnInfo val format: String,
    @ColumnInfo val bitmap: Bitmap
)
