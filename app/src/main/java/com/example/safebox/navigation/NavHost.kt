package com.example.safebox.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.safebox.navigation.Screens.DetailScreen
import com.example.safebox.navigation.Screens.MainScreen
import com.example.safebox.screens.DetailScreen
import com.example.safebox.screens.MainScreen

@Composable
fun AppNavHost(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = MainScreen){
        composable<MainScreen>{
            MainScreen()
        }
        composable<DetailScreen>{
            DetailScreen()
        }
    }
}
