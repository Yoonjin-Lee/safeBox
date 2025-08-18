package com.example.safebox.navigation

import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.safebox.navigation.Screens.AddScreen
import com.example.safebox.navigation.Screens.DecodeScreenRoute
import com.example.safebox.navigation.Screens.EncodeScreen
import com.example.safebox.navigation.Screens.MainScreen
import com.example.safebox.screens.DecodeScreen
import com.example.safebox.screens.EncodeScreen
import com.example.safebox.screens.MainScreen
import com.example.safebox.viewModel.MainViewModel

@Composable
fun AppNavHost() {
    val entryDecorator = rememberSavedStateNavEntryDecorator()
    val backStack = rememberNavBackStack(
        MainScreen
    )

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(entryDecorator),
        entryProvider = entryProvider {
            entry(MainScreen) {
                MainScreen(
                    goToAddScreen = { backStack.add(AddScreen) },
                    goToDetailScreen = {
                        backStack.add(
                            DecodeScreenRoute(
                                it
                            )
                        )
                    }
                )
            }
            entry(AddScreen) {
                com.example.safebox.screens.AddScreen(
                    onBack = { backStack.removeLastOrNull() },
                    onNext = { backStack.add(EncodeScreen) }
                )
            }
            entry(EncodeScreen) {
                val vm = hiltViewModel<MainViewModel>()
                EncodeScreen(
                    onBack = {
                        backStack.removeLastOrNull()
                        backStack.removeLastOrNull()
                    },
                    bitmap = vm.selectedBitmap,
                    onEncode = { bitmap, key ->
                        val bitmaps = vm.splitBitmap(bitmap)
                        val bitmapParts = vm.encodeBitmap(bitmaps, key)
                        vm.saveBitmapParts(bitmapParts)
                    }
                )
            }
            entry<DecodeScreenRoute> {
                DecodeScreen(
                    onBackClick = { backStack.removeLastOrNull() },
                    decodedByteArrays = it.byteList,
                )
            }
        }
    )
}
