package com.yoonjin.safebox.domain.usecase

import com.yoonjin.safebox.common.BaseUseCase
import com.yoonjin.safebox.domain.entity.ImageEntity
import com.yoonjin.safebox.domain.repository.BitmapRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBitmapsUseCase @Inject constructor(
    private val bitmapRepository: BitmapRepository
)
    : BaseUseCase<Unit, Flow<List<ImageEntity>>>(){
    override suspend fun execute(param: Unit): Flow<List<ImageEntity>> {
        return bitmapRepository.getBitmapList()
    }
}