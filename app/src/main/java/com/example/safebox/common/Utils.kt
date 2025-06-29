package com.example.safebox.common

import android.graphics.Bitmap
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object Utils {
    private const val SECRET_KEY = "0123456789abcdef0123456789abcdef" // 32글자 (256비트)
    private const val TRANSFORMATION = "AES/ECB/PKCS5Padding"

    private val secretKeySpec = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")

    fun encrypt(data: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
        return cipher.doFinal(data)
    }

    fun decrypt(data: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
        return cipher.doFinal(data)
    }
}