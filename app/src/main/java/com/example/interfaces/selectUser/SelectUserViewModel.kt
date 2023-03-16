package com.example.interfaces.selectUser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.interfaces.Session
import com.example.interfaces.models.User

class SelectUserViewModel : ViewModel() {
    val usersL = MutableLiveData<List<User>>()

    fun loadUsers() {
        usersL.postValue(Session.getUsers())
    }
}