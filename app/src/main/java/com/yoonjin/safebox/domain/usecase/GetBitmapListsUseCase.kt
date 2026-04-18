package com.yoonjin.safebox.domain.usecase

import com.yoonjin.safebox.common.BaseUseCase
import com.yoonjin.safebox.domain.entity.ImageListEntity
import com.yoonjin.safebox.domain.repository.BitmapRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBitmapListsUseCase @Inject constructor(
    private val bitmapRepository: BitmapRepository
)
    : BaseUseCase<Unit, Flow<List<ImageListEntity>>>(){
    override suspend fun execute(param: Unit): Flow<List<ImageListEntity>> {
        return bitmapRepository.getBitmapList()
    }
}