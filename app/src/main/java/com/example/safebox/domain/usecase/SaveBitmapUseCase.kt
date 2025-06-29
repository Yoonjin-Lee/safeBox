package com.example.safebox.domain.usecase

import android.graphics.Bitmap
import android.util.Log
import com.example.safebox.common.BaseUseCase
import com.example.safebox.domain.repository.BitmapRepository
import jakarta.inject.Inject

/**
 * 쪼개진 Bitmap을 Local DB에 저장
 */
class SaveBitmapUseCase @Inject constructor(
    private val bitmapRepository: BitmapRepository
)
    : BaseUseCase<List<Bitmap>, Unit>(){
    override suspend fun execute(param: List<Bitmap>) {
        for (bitmap in param){
            try {
                bitmapRepository.saveBitmap(bitmap)
            }catch (e : Exception){
                Log.e("SaveBitmapUseCase", "execute: ", e)
            }
        }
        bitmapRepository.increaseImageCounter()
    }
}