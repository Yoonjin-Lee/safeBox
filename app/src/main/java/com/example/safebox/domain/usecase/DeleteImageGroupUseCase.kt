package com.example.safebox.domain.usecase

import com.example.safebox.common.BaseUseCase
import com.example.safebox.domain.repository.BitmapRepository
import javax.inject.Inject

class DeleteImageGroupUseCase @Inject constructor(
    private val bitmapRepository: BitmapRepository
) : BaseUseCase<String, Unit>() {
    override suspend fun execute(param: String) {
        bitmapRepository.deleteImageGroup(param)
    }
}
