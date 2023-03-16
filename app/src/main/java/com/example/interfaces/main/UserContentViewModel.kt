package com.example.interfaces.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.interfaces.Session
import com.example.interfaces.models.Content
import com.example.interfaces.models.User
import kotlinx.coroutines.*
import kotlin.random.Random

class UserContentViewModel : ViewModel() {
    val userL = MutableLiveData<User>()
    val contentsL = MutableLiveData<List<Content>>()

    fun loadUserData() {
        userL.postValue(Session.currentUser)
    }

    fun toFavourite(content: Content) {
        Session.toFavourite(content)
    }

    fun loadContent() {
        CoroutineScope(Dispatchers.IO).launch {
            val randMills = Random.nextLong(until = 2000)
            delay(randMills)
            contentsL.postValue(Session.getContent())
        }
    }
}