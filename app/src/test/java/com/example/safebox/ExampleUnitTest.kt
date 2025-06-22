package com.example.safebox

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.test.core.app.ApplicationProvider
import com.example.safebox.viewModel.MainViewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
class ExampleUnitTest {
    val viewModel = MainViewModel()
    val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun splitBitmapTest() {
        val img = ContextCompat.getDrawable(context, R.drawable.ipad)!! //png 파일
        val bitmap = img.toBitmap()

        val bitmapList = viewModel.splitBitmap(bitmap)
        assertThat(bitmapList).hasSize(3)
    }
}