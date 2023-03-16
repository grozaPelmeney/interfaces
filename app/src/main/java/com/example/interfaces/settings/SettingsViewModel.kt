package com.example.interfaces.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.interfaces.Session
import com.example.interfaces.models.Setting
import com.example.interfaces.models.User

class SettingsViewModel : ViewModel() {
    val settingsL = MutableLiveData<List<Setting>>()

    val user by lazy {
        if (userInit != null) {
            val buf = userInit
            userInit = null
            return@lazy buf
        }
        return@lazy null
    }

    fun loadSettings() {
        val settings = Session.getSettings()
        settingsL.postValue(settings)
    }

    companion object {
        var userInit: User? = null
    }
}