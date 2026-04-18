package com.yoonjin.safebox.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.yoonjin.safebox.navigation.Screens.AddScreen
import com.yoonjin.safebox.navigation.Screens.DecodeScreenRoute
import com.yoonjin.safebox.navigation.Screens.EncodeScreen
import com.yoonjin.safebox.navigation.Screens.MainScreen
import com.yoonjin.safebox.screens.DecodeScreen
import com.yoonjin.safebox.screens.EncodeScreen
import com.yoonjin.safebox.screens.MainScreen
import com.yoonjin.safebox.viewModel.MainViewModel

@Composable
fun AppNavHost(contentPadding: PaddingValues = PaddingValues()) {
    val backStack = rememberNavBackStack(MainScreen)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        modifier = androidx.compose.ui.Modifier.padding(contentPadding),
        entryProvider = entryProvider {
            entry(MainScreen) {
                MainScreen(
                    goToAddScreen = { backStack.add(AddScreen) },
                    goToDetailScreen = { name, key ->
                        backStack.add(DecodeScreenRoute(name = name, key = key))
                    }
                )
            }
            entry(AddScreen) {
                com.yoonjin.safebox.screens.AddScreen(
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
                val vm = androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel<MainViewModel>()
                DecodeScreen(
                    onBackClick = { backStack.removeLastOrNull() },
                    selectedBitmapName = it.name,
                    decryptKey = it.key,
                    viewModel = vm
                )
            }
        }
    )
}
