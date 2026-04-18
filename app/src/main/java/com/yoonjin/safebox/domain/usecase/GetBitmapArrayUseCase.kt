package com.yoonjin.safebox.domain.usecase

import com.yoonjin.safebox.common.BaseUseCase
import com.yoonjin.safebox.domain.repository.BitmapRepository
import javax.inject.Inject

class GetBitmapArrayUseCase @Inject constructor(
    private val bitmapRepository: BitmapRepository
) : BaseUseCase<String, List<ByteArray>>(){
    override suspend fun execute(param: String): List<ByteArray> {
        return bitmapRepository.getBitmapArray(param)
    }
}