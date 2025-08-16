package com.example.safebox.common

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object Utils {
    private const val ALGORITHM = "AES/CBC/PKCS5Padding"
    // ====== 암호화 파라미터 ======
    private const val VERSION: Byte = 1
    private const val SALT_LEN = 16            // 128-bit salt
    private const val IV_LEN = 12              // GCM 권장 96-bit IV
    private const val KEY_LEN_BITS = 256       // AES-256
    private const val PBKDF2_ITER = 100_000    // 기기 성능 따라 조정 가능
    private const val GCM_TAG_BITS = 128       // 16바이트 태그

    private val secureRandom = SecureRandom()

    private fun deriveKey(password: String, salt: ByteArray): SecretKeySpec {
        val spec = PBEKeySpec(password.toCharArray(), salt, PBKDF2_ITER, KEY_LEN_BITS)
        val skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val keyBytes = skf.generateSecret(spec).encoded
        return SecretKeySpec(keyBytes, "AES")
    }


    fun encrypt(data: ByteArray, key: String): ByteArray {
        val salt = ByteArray(SALT_LEN).also { secureRandom.nextBytes(it) }
        val iv = ByteArray(IV_LEN).also { secureRandom.nextBytes(it) }
        val secretKey = deriveKey(key, salt)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, GCMParameterSpec(GCM_TAG_BITS, iv))
        val cipherText = cipher.doFinal(data)

        // [version][salt][iv][ciphertext+tag]
        return ByteBuffer.allocate(1 + SALT_LEN + IV_LEN + cipherText.size)
            .put(VERSION)
            .put(salt)
            .put(iv)
            .put(cipherText)
            .array()
    }

    fun decrypt(encryptedData: ByteArray, key: String): ByteArray {
        require(encryptedData.size >= 1 + SALT_LEN + IV_LEN) { "Invalid ciphertext" }
        val buf = ByteBuffer.wrap(encryptedData)

        val ver = buf.get()
        require(ver == VERSION) { "Unsupported version: $ver" }

        val salt = ByteArray(SALT_LEN).also { buf.get(it) }
        val iv = ByteArray(IV_LEN).also { buf.get(it) }
        val cipherBytes = ByteArray(buf.remaining()).also { buf.get(it) }

        val secretKey = deriveKey(key, salt)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(GCM_TAG_BITS, iv))
        return cipher.doFinal(cipherBytes)
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}