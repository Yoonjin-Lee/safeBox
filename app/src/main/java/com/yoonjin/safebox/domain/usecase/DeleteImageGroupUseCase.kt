package com.yoonjin.safebox.domain.usecase

import com.yoonjin.safebox.common.BaseUseCase
import com.yoonjin.safebox.domain.repository.BitmapRepository
import javax.inject.Inject

class DeleteImageGroupUseCase @Inject constructor(
    private val bitmapRepository: BitmapRepository
) : BaseUseCase<String, Unit>() {
    override suspend fun execute(param: String) {
        bitmapRepository.deleteImageGroup(param)
    }
}
