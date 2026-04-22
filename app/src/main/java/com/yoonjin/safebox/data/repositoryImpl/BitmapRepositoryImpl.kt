package com.yoonjin.safebox.data.repositoryImpl

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.yoonjin.safebox.common.Constant
import com.yoonjin.safebox.data.dao.ImageDao
import com.yoonjin.safebox.data.dto.toEntity
import com.yoonjin.safebox.data.dto.toImageDto
import com.yoonjin.safebox.domain.entity.ImageListEntity
import com.yoonjin.safebox.domain.repository.BitmapRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class BitmapRepositoryImpl @Inject constructor(
    private val imageDao: ImageDao,
    private val dataStore: DataStore<Preferences>
) : BitmapRepository {
    override suspend fun saveBitmap(bitmap: ByteArray, name: String, groupName: String) {
        imageDao.insert(
            bitmap.toImageDto(format = "PNG", name = name, groupName = groupName)
        )
    }

    override suspend fun getBitmapList(): Flow<List<ImageListEntity>> {
        return imageDao.getAllFlow().map { list ->
            list.map { it.toEntity() }
        }
    }

    override suspend fun deleteImageGroup(name: String) {
        imageDao.deleteByGroupName(name)
    }

    override suspend fun getBitmapArrayByGroupName(name: String): List<ByteArray> {
        val result = imageDao.getImageByGroupName(name)
        Log.d("SafeBoxLog", "getBitmapArrayByGroupName result: ${result.size}")
        return result
    }

    override suspend fun getGroupName(): String {
        val key = stringPreferencesKey(Constant.NUMBER_OF_IMAGES.name)
        val groupName = dataStore.data.first()[key] ?: "0"

        return groupName
    }

    override suspend fun setGroupName(groupName: String) {
        val key = stringPreferencesKey(Constant.NUMBER_OF_IMAGES.name)
        dataStore.edit {
            it[key] = (groupName.toInt() + 1).toString()
        }
    }
}