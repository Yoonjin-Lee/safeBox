package com.yoonjin.safebox.data.dto

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yoonjin.safebox.domain.entity.ImageEntity
import java.io.ByteArrayOutputStream
import java.util.UUID

@Entity
data class ImageDto(
    @PrimaryKey val uuid: String,
    @ColumnInfo val format: String,
    @ColumnInfo val name: String? = null,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val bitmap: ByteArray,
    @ColumnInfo(defaultValue = "") val groupName: String
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImageDto) return false
        return uuid   == other.uuid   &&
                format == other.format &&
                bitmap.contentEquals(other.bitmap) &&
                groupName == other.groupName
    }

    override fun hashCode(): Int {
        var result = uuid.hashCode()
        result = 31 * result + format.hashCode()
        result = 31 * result + bitmap.contentHashCode() // ▲ 핵심
        return result
    }
}

fun ByteArray.toImageDto(format: String, name: String? = null, groupName: String) : ImageDto {
    return ImageDto(
        uuid = UUID.randomUUID().toString(),
        format = format,
        bitmap = this,
        name = name,
        groupName = groupName
    )
}