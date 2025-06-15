package com.example.safebox.data.dto

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageDto(
    @PrimaryKey val uuid: String,
    @ColumnInfo val format: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val bitmap: ByteArray
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImageDto) return false
        return uuid   == other.uuid   &&
                format == other.format &&
                bitmap.contentEquals(other.bitmap)   // ▲ 핵심
    }

    override fun hashCode(): Int {
        var result = uuid.hashCode()
        result = 31 * result + format.hashCode()
        result = 31 * result + bitmap.contentHashCode() // ▲ 핵심
        return result
    }
}
