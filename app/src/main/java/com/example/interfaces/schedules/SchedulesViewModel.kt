package com.example.interfaces.schedules

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.TestDataGenerator
import com.example.interfaces.Session
import com.example.interfaces.models.Setting
import com.example.interfaces.models.User

class SchedulesViewModel : ViewModel() {
    val scheduleOnL = MutableLiveData<List<Setting>>()
    val scheduleOffL = MutableLiveData<List<Setting>>()

    val user by lazy {
        if (userInit != null) {
            val buf = userInit
            userInit = null
            return@lazy buf
        }
        return@lazy null
    }

    fun loadSchedules() {
        scheduleOnL.postValue(TestDataGenerator.getSchedules())
        scheduleOffL.postValue(TestDataGenerator.getSchedules())
    }

    companion object {
        var userInit: User? = null
    }
}