package com.yoonjin.safebox.viewModel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yoonjin.safebox.common.Utils
import com.yoonjin.safebox.domain.entity.ImageListEntity
import com.yoonjin.safebox.domain.usecase.DeleteImageGroupUseCase
import com.yoonjin.safebox.domain.usecase.GetBitmapArrayUseCase
import com.yoonjin.safebox.domain.usecase.GetBitmapListsUseCase
import com.yoonjin.safebox.domain.usecase.SaveBitmapUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val saveBitmapUseCase: SaveBitmapUseCase,
    private val getBitmapListsUseCase: GetBitmapListsUseCase,
    private val deleteImageGroupUseCase: DeleteImageGroupUseCase,
    private val getBitmapArrayUseCase: GetBitmapArrayUseCase
) : ViewModel() {
    private val _bitmaps = MutableStateFlow<List<ImageListEntity>>(emptyList())
    val bitmaps: StateFlow<List<ImageListEntity>> = _bitmaps.asStateFlow()

    private val _selectedBitmap = MutableStateFlow<Bitmap?>(null)
    val selectedBitmap: StateFlow<Bitmap?> = _selectedBitmap.asStateFlow()

    fun setSelectedBitmap(bitmap: Bitmap) {
        _selectedBitmap.value = bitmap
    }

    private val _encodedBitmaps = MutableStateFlow<List<ByteArray>>(emptyList())
    val encodedBitmaps: StateFlow<List<ByteArray>> = _encodedBitmaps.asStateFlow()

    /**
     * 사용자가 선택한 Uri의 bitmap을 3등분한다
     * params - bitmap, num of chunks
     */
    fun splitBitmap(bitmap: Bitmap, num: Int = 3)
            : List<Bitmap> {
        val width = bitmap.width
        val height = bitmap.height
        val partWidth = width / num

        val bitmaps: ArrayList<Bitmap> = arrayListOf()

        for (i in 0 until num) {
            val startX = i * partWidth
            val chunkWidth = if (i == num - 1) width - startX else partWidth
            bitmaps.add(Bitmap.createBitmap(bitmap, startX, 0, chunkWidth, height))
        }

        return bitmaps

    }

    /**
     * 쪼개진 bitmap을 저장한다
     */
    fun saveBitmapParts(params: List<ByteArray>, title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            saveBitmapUseCase(SaveBitmapUseCase.Params(parts = params, name = title))
        }
    }

    fun deleteImageGroup(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteImageGroupUseCase(name)
        }
    }

    /**
     * bitmap을 사용자가 지정한 제목으로 인코딩한다
     */
    fun encodeBitmap(params: List<Bitmap>, key: String): List<ByteArray> {
        return params.map { bitmap ->
            Utils.encrypt(Utils.bitmapToByteArray(bitmap), key)
        }
    }

    /**
     * 인코딩된 bitmap을 사용자가 지정한 이름으로 디코딩한다
     */
    fun decodeBitmap(params: List<ByteArray>, key: String): List<ByteArray> {
        return params.map { byteArray ->
            Utils.decrypt(byteArray, key)
        }
    }

    suspend fun getDecodedBitmaps(name: String, key: String): List<ByteArray> {
        val encryptedList = getBitmapArrayUseCase(name)
        return decodeBitmap(encryptedList, key)
    }

    init {
        viewModelScope.launch {
            getBitmapListsUseCase(Unit).collectLatest {
                _bitmaps.value = it

                Log.d("MainViewModel", "bitmaps: ${it.size}")
            }
        }
    }
}