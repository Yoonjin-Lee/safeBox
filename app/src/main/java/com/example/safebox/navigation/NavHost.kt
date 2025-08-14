package com.example.safebox.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.safebox.navigation.Screens.AddScreen
import com.example.safebox.navigation.Screens.MainScreen
import com.example.safebox.screens.MainScreen

@Composable
fun AppNavHost(){
    val backStack = rememberNavBackStack(MainScreen)

    NavDisplay(
        backStack = backStack,
        onBack = {backStack.removeLastOrNull()},
        entryProvider = entryProvider {
            entry(MainScreen){
                MainScreen(
                    goToAddScreen = {backStack.add(AddScreen)}
                )
            }
            entry(AddScreen){
                com.example.safebox.screens.AddScreen()
            }
        }
    )
}
