package com.example.safebox.viewModel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    /**
     * 사용자가 선택한 Uri의 bitmap을 3등분한다
     * params - bitmap, num of chunks
     */
    fun splitBitmap(bitmap: Bitmap, num: Int = 3)
    : List<Bitmap> {
        val width = bitmap.width
        val height = bitmap.height
        val partWidth = width / num

        val bitmaps : ArrayList<Bitmap> = arrayListOf()

        for (i in 1..num){
            val startX = i * partWidth
            val chunkWidth = if (i == num - 1) height - startX else partWidth
            val chunk = Bitmap.createBitmap(bitmap, startX, 0, chunkWidth, height)
            bitmaps.add(chunk)
        }

        return bitmaps

    }

    /**
     * 쪼개진 bitmap을 저장한다
     */
    fun saveBitmapParts(){


    }
}