package com.example.safebox.data.repositoryImpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.safebox.data.dao.ImageDao
import com.example.safebox.data.dto.toEntity
import com.example.safebox.data.dto.toImageDto
import com.example.safebox.domain.entity.ImageEntity
import com.example.safebox.domain.repository.BitmapRepository
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

    override suspend fun getBitmapList(): Flow<List<ImageEntity>> {
        return imageDao.getAllFlow().map { list ->
            list.map { it.toEntity() }
        }
    }

    override suspend fun deleteImageGroup(name: String) {
        imageDao.deleteByName(name)
    }
}