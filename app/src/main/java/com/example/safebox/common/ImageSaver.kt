package com.example.safebox.common

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream

object ImageSaver {
    enum class ImageFormat(val mime: String, val compress: Bitmap.CompressFormat, val defaultExt: String) {
        JPEG("image/jpeg", Bitmap.CompressFormat.JPEG, "jpg"),
        PNG("image/png", Bitmap.CompressFormat.PNG, "png"),
    }

    /**
     * @return 저장된 항목의 Uri (실패 시 null)
     */
    fun saveBitmapToGallery(
        context: Context,
        bitmap: Bitmap,
        baseFileName: String = "IMG_${System.currentTimeMillis()}",
        format: ImageFormat = ImageFormat.JPEG,
        quality: Int = 95,
        relativeDir: String = Environment.DIRECTORY_PICTURES + "/SaveBox"
    ): Uri? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveOnQAndAbove(context, bitmap, baseFileName, format, quality, relativeDir)
        } else {
            saveOnPreQ(context, bitmap, baseFileName, format, quality)
        }
    }

    @SuppressLint("InlinedApi")
    private fun saveOnQAndAbove(
        context: Context,
        bitmap: Bitmap,
        baseFileName: String,
        format: ImageFormat,
        quality: Int,
        relativeDir: String
    ): Uri? {
        val resolver = context.contentResolver
        val fileName = "$baseFileName.${format.defaultExt}"

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, format.mime)
            put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            put(MediaStore.Images.Media.IS_PENDING, 1)
            put(MediaStore.Images.Media.RELATIVE_PATH, relativeDir) // 예: Pictures/MyApp
        }

        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values) ?: return null

        try {
            resolver.openOutputStream(uri)?.use { out ->
                if (!bitmap.compress(format.compress, quality, out)) throw RuntimeException("Bitmap compress failed")
            }
            values.clear()
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(uri, values, null, null)
            return uri
        } catch (e: Exception) {
            // 실패 시 항목 제거
            resolver.delete(uri, null, null)
            e.printStackTrace()
            return null
        }
    }

    private fun saveOnPreQ(
        context: Context,
        bitmap: Bitmap,
        baseFileName: String,
        format: ImageFormat,
        quality: Int
    ): Uri? {
        // 권한: WRITE_EXTERNAL_STORAGE 필요 (Android 9 이하)
        val pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val appDir = File(pictures, "MyApp").apply { if (!exists()) mkdirs() }
        val file = File(appDir, "$baseFileName.${format.defaultExt}")

        return try {
            FileOutputStream(file).use { out ->
                if (!bitmap.compress(format.compress, quality, out)) throw RuntimeException("Bitmap compress failed")
            }
            // 갤러리 스캔
            MediaScannerConnection.scanFile(
                context,
                arrayOf(file.absolutePath),
                arrayOf(format.mime),
                null
            )
            // MediaStore에 등록된 Uri를 다시 조회
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DATA, file.absolutePath)
                put(MediaStore.Images.Media.MIME_TYPE, format.mime)
                put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            }
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}