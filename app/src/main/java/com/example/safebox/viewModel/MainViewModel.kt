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
     */
    fun splitBitmap(bitmap: Bitmap){

    }

    /**
     * 쪼개진 bitmap을 저장한다
     */
    fun saveBitmapParts(){

    }
}