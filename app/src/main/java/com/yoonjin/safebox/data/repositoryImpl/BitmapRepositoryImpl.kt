package com.yoonjin.safebox.data.repositoryImpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.yoonjin.safebox.data.dao.ImageDao
import com.yoonjin.safebox.data.dto.toEntity
import com.yoonjin.safebox.data.dto.toImageDto
import com.yoonjin.safebox.domain.entity.ImageEntity
import com.yoonjin.safebox.domain.entity.ImageListEntity
import com.yoonjin.safebox.domain.repository.BitmapRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BitmapRepositoryImpl @Inject constructor(
    private val imageDao: ImageDao,
    private val dataStore: DataStore<Preferences>
) : BitmapRepository {
    override suspend fun saveBitmap(bitmap: ByteArray, name: String) {
        imageDao.insert(
            bitmap.toImageDto(format = "PNG", name = name)
        )
    }

    override suspend fun getBitmapList(): Flow<List<ImageListEntity>> {
        return imageDao.getAllFlow().map { list ->
            list.map { it.toEntity() }
        }
    }

    override suspend fun deleteImageGroup(name: String) {
        imageDao.deleteByName(name)
    }

    override suspend fun getBitmapArray(name: String): List<ByteArray> {
        return imageDao.getImageByName(name)
    }
}