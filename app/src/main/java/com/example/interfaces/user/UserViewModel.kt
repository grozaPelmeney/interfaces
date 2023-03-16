package com.example.interfaces.user

import androidx.lifecycle.ViewModel
import com.example.interfaces.Session
import com.example.interfaces.models.User

class UserViewModel : ViewModel() {
    val user by lazy {
        if (isNewUserInit) return@lazy null

        if (userInit != null) {
            val buf = userInit
            userInit = null
            return@lazy buf
        }
        return@lazy null
    }
    val isNewUser = isNewUserInit

    fun validateUsername(username: String?): Boolean {
        val isUsernameGood = !username.isNullOrBlank()

        if (isUsernameGood) {
            user?.name = username!!
        }

        return isUsernameGood
    }

    fun updateUser() {
        user?.let {
            Session.updateCurrentUser(it)
        }
    }

    fun addUser(username: String) {
        val newUser = User(
            id = Session.getUsers().size + 1,
            name = username
        )
        Session.addUser(newUser)
    }

    fun deleteUser() {
        user?.let {
            Session.removeUser(it)
        }
    }

    companion object {
        var userInit: User? = null
        var isNewUserInit: Boolean = false //false - редактирование, true - новый пользователь
    }
}