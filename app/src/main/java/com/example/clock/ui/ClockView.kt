package com.example.clock.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.View
import java.util.Calendar

class ClockView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val hourHandColor = Color.BLACK
    private val minuteHandColor = Color.BLACK
    private val secondHandColor = Color.RED

    private val hourHandLength = 0.5f
    private val minuteHandLength = 0.75f
    private val secondHandLength = 0.9f

    private val handStrokeWidth = 5f

    private val calendar = Calendar.getInstance()

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    init {
        startTimeUpdates()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = Math.min(centerX, centerY)

        // Draw clock face
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        canvas.drawCircle(centerX, centerY, radius, paint)

        // Draw hour hand
        paint.color = hourHandColor
        paint.strokeWidth = handStrokeWidth
        val hourAngle = (calendar.get(Calendar.HOUR_OF_DAY) * 30 + calendar.get(Calendar.MINUTE) / 2).toFloat()
        drawHand(canvas, centerX, centerY, radius * hourHandLength, hourAngle)

        // Draw minute hand
        paint.color = minuteHandColor
        val minuteAngle = (calendar.get(Calendar.MINUTE) * 6 + calendar.get(Calendar.SECOND) / 10).toFloat()
        drawHand(canvas, centerX, centerY, radius * minuteHandLength, minuteAngle)

        // Draw second hand
        paint.color = secondHandColor
        val secondAngle = calendar.get(Calendar.SECOND) * 6f
        drawHand(canvas, centerX, centerY, radius * secondHandLength, secondAngle)
    }

    private fun drawHand(canvas: Canvas, centerX: Float, centerY: Float, length: Float, angle: Float) {
        val startX = centerX
        val startY = centerY
        val endX = centerX + Math.cos(Math.toRadians(angle.toDouble())) * length
        val endY = centerY - Math.sin(Math.toRadians(angle.toDouble())) * length
        canvas.drawLine(startX, startY, endX.toFloat(), endY.toFloat(), paint)
    }

    private fun startTimeUpdates() {
        val timeUpdateHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                calendar.timeInMillis = System.currentTimeMillis()
                invalidate()

                sendEmptyMessageDelayed(0, 1000)
            }
        }

        timeUpdateHandler.sendEmptyMessage(0)
    }
}
