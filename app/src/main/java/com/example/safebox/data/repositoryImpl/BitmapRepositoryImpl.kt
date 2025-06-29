package com.example.safebox.data.repositoryImpl

import android.graphics.Bitmap
import com.example.safebox.data.dao.ImageDao
import com.example.safebox.data.dto.toImageDto
import com.example.safebox.domain.repository.BitmapRepository
import jakarta.inject.Inject

class BitmapRepositoryImpl @Inject constructor(
    private val imageDao: ImageDao
): BitmapRepository {
    override suspend fun saveBitmap(bitmap: Bitmap) {
        imageDao.insert(
            bitmap.toImageDto(format = "PNG")
        )
    }
}