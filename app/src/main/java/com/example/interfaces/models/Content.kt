package com.example.interfaces.models

class Content(
    val id: Int,
    val name: String,
    val imgUrl: String? = null,
    val description: String? = null,
    val type: ContentType,
)