package com.example.safebox

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.test.core.app.ApplicationProvider
import com.example.safebox.domain.repository.BitmapRepository
import com.example.safebox.domain.usecase.SaveBitmapUseCase
import com.example.safebox.viewModel.MainViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bouncycastle.util.test.SimpleTest.runTest
import org.junit.Before
import org.junit.Test

class BitmapSaveTest {
    private lateinit var bitmapRepository: BitmapRepository
    private lateinit var saveBitmapUseCase: SaveBitmapUseCase
    val viewModel = MainViewModel()
    val context = ApplicationProvider.getApplicationContext<Context>()
    @Before
    fun setUp(){
        bitmapRepository = mockk<BitmapRepository>(relaxed = true)
        saveBitmapUseCase = SaveBitmapUseCase(bitmapRepository)
    }

    @Test
    fun saveBitmapParts(){
        val img = ContextCompat.getDrawable(context, R.drawable.ipad)!! //png 파일
        val bitmap = img.toBitmap()

        val bitmapList = viewModel.splitBitmap(bitmap)
    }
}