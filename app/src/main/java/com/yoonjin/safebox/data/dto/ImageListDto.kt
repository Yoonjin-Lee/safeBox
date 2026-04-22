package com.yoonjin.safebox.data.dto

import com.yoonjin.safebox.domain.entity.ImageListEntity

data class ImageListDto(
    val uuid: String,
    val name: String,
    val format: String,
    val groupName: String
)

fun ImageListDto.toEntity(): ImageListEntity{
    return ImageListEntity(
        uuid = uuid,
        name = name,
        format = format,
        groupName = groupName
    )
}