package com.example.safebox.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    /**
     * 사용자가 선택한 Uri의
     */
    fun divideUri(uri: Uri) : Triple<String, String, String> {
        val uriChars = uri.toString().toCharArray()
        val size = uriChars.size
        return Triple(
            first = uriChars.concatToString(0, size/3),
            second = uriChars.concatToString(size/3, size/3 * 2),
            third = uriChars.concatToString(size/3 * 2, size)
        )
    }
}