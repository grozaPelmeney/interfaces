package com.example.interfaces.models

class User(val id: Int, var name: String) {
    var likes = listOf<Content>()
}