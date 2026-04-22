package com.yoonjin.safebox.domain.usecase

import android.util.Log
import com.yoonjin.safebox.common.BaseUseCase
import com.yoonjin.safebox.domain.repository.BitmapRepository
import jakarta.inject.Inject

/**
 * 쪼개진 Bitmap을 Local DB에 저장
 */
class SaveBitmapUseCase @Inject constructor(
    private val bitmapRepository: BitmapRepository
) : BaseUseCase<SaveBitmapUseCase.Params, Unit>() {

    data class Params(val parts: List<ByteArray>, val name: String)

    override suspend fun execute(param: Params) {
        val groupName = bitmapRepository.getGroupName()
        param.parts.forEach { bitmap ->
            try {
                bitmapRepository.saveBitmap(bitmap, param.name, groupName)
            } catch (e: Exception) {
                Log.e("SaveBitmapUseCase", "execute: ", e)
            }
        }
        bitmapRepository.setGroupName(groupName)
    }
}