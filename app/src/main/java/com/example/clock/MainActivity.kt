package com.example.clock

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.clock.ui.ClockView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getWindow().setBackgroundDrawableResource(R.color.ltblue);
    }
}
