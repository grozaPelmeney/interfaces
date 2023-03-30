package com.example.interfaces.tv_program

import android.text.Spanned
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.TestDataGenerator
import com.example.interfaces.models.Content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class TvProgramViewModel : ViewModel() {

    val scheduleL = MutableLiveData<Spanned>()

    val tvChannel by lazy {
        if (tvChannelInit != null) {
            val buf = tvChannelInit
            tvChannelInit = null
            return@lazy buf
        }
        return@lazy null
    }

    fun getSchedule() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(Random.nextLong(until = 200))
            scheduleL.postValue(TestDataGenerator.getTvProgramText())
        }
    }

    companion object {
        var tvChannelInit: Content? = null
    }
}