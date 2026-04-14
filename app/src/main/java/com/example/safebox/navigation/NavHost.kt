package com.example.safebox.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
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
    val backStack = rememberNavBackStack(
        MainScreen
    )

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
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
                val bitmap by vm.selectedBitmap.collectAsState()
                EncodeScreen(
                    onBack = {
                        backStack.removeLastOrNull()
                        backStack.removeLastOrNull()
                    },
                    bitmap = bitmap,
                    onEncode = { bmp, title, key ->
                        val bitmaps = vm.splitBitmap(bmp)
                        val bitmapParts = vm.encodeBitmap(bitmaps, key)
                        vm.saveBitmapParts(bitmapParts, title)
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
