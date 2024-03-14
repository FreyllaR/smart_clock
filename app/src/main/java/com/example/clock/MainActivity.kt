package com.example.clock

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.clock.ui.ClockView

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val clockView = ClockView(this)
        val layout = findViewById<ConstraintLayout>(R.id.layout)
        layout.addView(clockView)
    }
}