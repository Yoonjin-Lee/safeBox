package com.example.safebox.domain.entity

data class ImageEntity(
    val id: String,
    val byteArray: ByteArray,
    val name: String?,
    val format: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageEntity

        if (id != other.id) return false
        if (!byteArray.contentEquals(other.byteArray)) return false
        if (name != other.name) return false
        if (format != other.format) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + byteArray.contentHashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + format.hashCode()
        return result
    }
}
