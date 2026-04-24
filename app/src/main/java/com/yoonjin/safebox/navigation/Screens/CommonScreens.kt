package com.yoonjin.safebox.navigation.Screens

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object MainScreen : NavKey

@Serializable
data class DecodeScreenRoute(
    val name: String,
    val groupName: String,
    val key: String,
) : NavKey

@Serializable
data object AddScreen : NavKey

@Serializable
data object EncodeScreen : NavKey