package com.example.safebox.viewModel

import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safebox.common.Utils
import com.example.safebox.domain.entity.ImageEntity
import com.example.safebox.domain.usecase.GetBitmapsUseCase
import com.example.safebox.domain.usecase.SaveBitmapUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val saveBitmapUseCase: SaveBitmapUseCase,
    private val getBitmapsUseCase: GetBitmapsUseCase
) : ViewModel() {
    private var _bitmaps = MutableStateFlow<List<ImageEntity>>(emptyList())
    val bitmaps: StateFlow<List<ImageEntity>> = _bitmaps

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
    fun saveBitmapParts(params: List<ByteArray>) {
        viewModelScope.launch(Dispatchers.IO) {
            saveBitmapUseCase(params)
        }
    }

    /**
     * bitmap을 사용자가 지정한 제목으로 인코딩한다
     */
    fun encodeBitmap(params: List<Bitmap>): List<ByteArray> {
        return params.map { ByteArrayOutputStream().use{
            it.toByteArray()
        } }
    }

    /**
     * 인코딩된 bitmap을 사용자가 지정한 이름으로 디코딩한다
     */
    fun decodeBitmap() {

    }

    init {
        viewModelScope.launch {
            getBitmapsUseCase(Unit).collectLatest {
                _bitmaps.value = it
                Log.d("MainViewModel", "bitmaps: ${it.size}")
            }
        }
    }
}