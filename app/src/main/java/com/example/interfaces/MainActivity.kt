package com.example.interfaces

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.interfaces.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.statusBarColor = getColor(R.color.status_bar_color)
    }
}