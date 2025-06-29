package com.example.safebox.domain.usecase

import com.example.safebox.common.BaseUseCase
import com.example.safebox.domain.entity.ImageEntity
import com.example.safebox.domain.repository.BitmapRepository
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