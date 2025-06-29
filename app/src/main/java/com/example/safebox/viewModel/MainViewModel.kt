package com.example.safebox.viewModel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safebox.domain.usecase.SaveBitmapUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val saveBitmapUseCase: SaveBitmapUseCase
) : ViewModel() {
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
    fun saveBitmapParts(params : List<Bitmap>) {
        viewModelScope.launch(Dispatchers.IO) {
            saveBitmapUseCase(params)
        }
    }

    /**
     * bitmap을 사용자가 지정한 제목으로 인코딩한다
     */
    fun encodeBitmap(){

    }

    /**
     * 인코딩된 bitmap을 사용자가 지정한 이름으로 디코딩한다
     */
    fun decodeBitmap(){

    }
}