package com.example.safebox.domain.repository

import android.graphics.Bitmap

interface BitmapRepository {
    suspend fun saveBitmap(bitmap: Bitmap)
}