package com.example.safebox.data.dto

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.safebox.domain.entity.ImageEntity
import java.io.ByteArrayOutputStream
import java.util.UUID
import kotlin.random.Random

@Entity
data class ImageDto(
    @PrimaryKey val uuid: String,
    @ColumnInfo val format: String,
    @ColumnInfo val name: String? = null,
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

fun Bitmap.toImageDto(format: String, name: String? = null): ImageDto {
    // format string에 따라 compress 형식을 지정
    val compressFormat = when (format) {
        "PNG" -> Bitmap.CompressFormat.PNG
        "JPEG" -> Bitmap.CompressFormat.JPEG
        "WEBP" -> Bitmap.CompressFormat.WEBP
        else -> throw IllegalArgumentException("Invalid format: $format")
    }

    val byteArray = ByteArrayOutputStream().use {
        this.compress(compressFormat, 100, it)
        it.toByteArray()
    }
    return ImageDto(
        uuid = UUID.randomUUID().toString(),
        format = format,
        bitmap = byteArray,
        name = name
    )
}

fun ImageDto.toEntity() : ImageEntity {
    return ImageEntity(
        id = uuid,
        format = format,
        byteArray = bitmap,
        name = name
    )
}

fun ByteArray.toImageDto(format: String, name: String? = null) : ImageDto {
    return ImageDto(
        uuid = UUID.randomUUID().toString(),
        format = format,
        bitmap = this,
        name = name
    )
}