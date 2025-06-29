package com.example.safebox.common

import android.util.Log

abstract class BaseUseCase<in Param, out Result>(
    private val tag: String = "SafeBoxLog",
) {
    suspend operator fun invoke(param: Param): Result {
        val className = this::class.java.simpleName
        Log.d(tag, "[START} $className, param: $param")

        val result = execute(param)

        Log.d(tag, "[END} $className, result: $result")
        return result
    }

    protected abstract suspend fun execute(param: Param): Result
}