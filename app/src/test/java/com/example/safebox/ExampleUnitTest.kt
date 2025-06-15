package com.example.safebox

import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import com.example.safebox.viewModel.MainViewModel
import org.junit.Test
import com.google.common.truth.Truth.assertThat

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    val viewModel = MainViewModel()

    @Test
    fun splitBitmapTest() {
        val img = R.drawable.ipad.toDrawable() //png 파일
        val bitmap = img.toBitmap()

        val bitmapList = viewModel.splitBitmap(bitmap)
        assertThat(bitmapList).hasSize(3)
    }
}