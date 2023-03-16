package com.example.interfaces.remoute

import androidx.lifecycle.ViewModel
import com.example.interfaces.models.User

class RemouteViewModel : ViewModel() {
    val user by lazy {
        if (userInit != null) {
            val buf = userInit
            userInit = null
            return@lazy buf
        }
        return@lazy null
    }

    companion object {
        var userInit: User? = null
    }
}