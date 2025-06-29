package com.example.safebox.data.repositoryImpl

import android.graphics.Bitmap
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.safebox.data.dao.ImageDao
import com.example.safebox.data.datastore.PreferenceKeys.IMAGE_COUNTER
import com.example.safebox.data.dto.toEntity
import com.example.safebox.data.dto.toImageDto
import com.example.safebox.domain.entity.ImageEntity
import com.example.safebox.domain.repository.BitmapRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.ByteArrayOutputStream

class BitmapRepositoryImpl @Inject constructor(
    private val imageDao: ImageDao,
    private val dataStore: DataStore<Preferences>
): BitmapRepository {
    override suspend fun saveBitmap(bitmap: ByteArray) {
        val counter = dataStore.data.first()[IMAGE_COUNTER] ?: "0"
        imageDao.insert(
            bitmap.toImageDto(format = "PNG", name = "image$counter")
        )
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    override suspend fun getBitmapList(): Flow<List<ImageEntity>> {
        return imageDao.getAllFlow().map { it -> it.map {
            val byteArray = it.bitmap.toUByteArray().toByteArray()
            it.toEntity(byteArray)
        } }
    }

    override suspend fun increaseImageCounter() {
        val counter = dataStore.data.first()[IMAGE_COUNTER] ?: "0"
        dataStore.edit {
            it[IMAGE_COUNTER] = (counter.toInt() + 1).toString()
        }
    }
}