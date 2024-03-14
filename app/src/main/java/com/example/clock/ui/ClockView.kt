package com.example.clock.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import java.time.ZoneId
import java.time.ZonedDateTime

class ClockView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val hourHandColor = Color.BLACK
    private val minuteHandColor = Color.BLACK
    private val secondHandColor = Color.RED

    private val hourHandLength = 0.5f
    private val minuteHandLength = 0.75f
    private val secondHandLength = 0.9f

    private val handStrokeWidth = 10f

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private var numbersRadius = 0f

    init {
        startTimeUpdates()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        numbersRadius = Math.min(w, h) / 2f * 0.85f - handStrokeWidth * 2.5f
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = Math.min(centerX, centerY - handStrokeWidth / 2) - handStrokeWidth * 2

        // Rotate canvas by -90 degrees
        canvas.rotate(-90f, centerX, centerY)

        // Draw clock face
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        canvas.drawCircle(centerX, centerY, radius, paint)

        // Draw hour marks
        paint.color = Color.BLACK
        paint.strokeWidth = 4f
        for (i in 0 until 12) {
            val angle = i * 30f
            val markLength = radius * 0.9f - (radius - handStrokeWidth * 2)
            val x =
                centerX + Math.cos(Math.toRadians(angle.toDouble())) * (radius - handStrokeWidth)
            val y =
                centerY - Math.sin(Math.toRadians(angle.toDouble())) * (radius - handStrokeWidth)
            val endX = x + Math.cos(Math.toRadians(angle.toDouble())) * markLength
            val endY = y - Math.sin(Math.toRadians(angle.toDouble())) * markLength
            canvas.drawLine(x.toFloat(), y.toFloat(), endX.toFloat(), endY.toFloat(), paint)
        }

        // Draw second marks between numbers
        paint.strokeWidth = 2f
        for (i in 0 until 60) {
            if (i % 5 != 0) {
                val angle = i * 6f
                val markLength = radius * 0.95f - (radius - handStrokeWidth * 2)
                val x =
                    centerX + Math.cos(Math.toRadians(angle.toDouble())) * (radius - handStrokeWidth)
                val y =
                    centerY - Math.sin(Math.toRadians(angle.toDouble())) * (radius - handStrokeWidth)
                val endX = x + Math.cos(Math.toRadians(angle.toDouble())) * markLength
                val endY = y - Math.sin(Math.toRadians(angle.toDouble())) * markLength
                canvas.drawLine(x.toFloat(), y.toFloat(), endX.toFloat(), endY.toFloat(), paint)
            }
        }

// Draw clock numbers
        paint.textSize = 70f
        paint.textAlign = Paint.Align.CENTER
        paint.style = Paint.Style.FILL
        paint.typeface = Typeface.DEFAULT_BOLD
        paint.color = Color.BLACK

        val numbersRadiusWithMargin = numbersRadius - handStrokeWidth * 2
        for (i in 1..12) {
            val angle = -i * 30f
            val x = centerX + Math.cos(Math.toRadians(angle.toDouble())) * numbersRadiusWithMargin
            val y = centerY - Math.sin(Math.toRadians(angle.toDouble())) * numbersRadiusWithMargin

// Rotate canvas by 90 degrees to draw numbers vertically
            canvas.save()
            canvas.rotate(90f, x.toFloat(), y.toFloat())
            canvas.drawText(i.toString(), x.toFloat(), y.toFloat(), paint)
            canvas.restore()
        }

// Draw clock border
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = handStrokeWidth * 2
        canvas.drawCircle(centerX, centerY, radius + handStrokeWidth, paint)

// Update center coordinates after drawing the clock border
        val newCenterX = centerX + handStrokeWidth / 2
        val newCenterY = centerY + handStrokeWidth / 2

// Get current time in Moscow timezone
        val moscowTimeZone = ZoneId.of("Europe/Moscow")
        val currentTime = ZonedDateTime.now(moscowTimeZone).toLocalTime()
        val hour = currentTime.hour
        val minute = currentTime.minute
        val second = currentTime.second

// Draw hour hand
        paint.color = hourHandColor
        paint.strokeWidth = handStrokeWidth
        val hourAngle = (hour * 30 + minute / 2f)
        drawHand(
            canvas,
            newCenterX,
            newCenterY,
            radius * hourHandLength,
            -hourAngle,
            handStrokeWidth
        )

// Draw minute hand
        paint.color = minuteHandColor
        val minuteAngle = minute * 6f
        drawHand(
            canvas,
            newCenterX,
            newCenterY,
            radius * minuteHandLength,
            -minuteAngle,
            handStrokeWidth
        )

// Draw second hand
        paint.color = secondHandColor
        val secondAngle = second * 6f
        drawHand(
            canvas,
            newCenterX,
            newCenterY,
            radius * secondHandLength,
            -secondAngle,
            handStrokeWidth
        )
    }

    private fun drawHand(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        length: Float,
        angle: Float,
        strokeWidth: Float
    ) {
        val startX = centerX
        val startY = centerY
        val endX = startX + Math.cos(Math.toRadians(angle.toDouble())) * length
        val endY = startY - Math.sin(Math.toRadians(angle.toDouble())) * length
        paint.strokeWidth = strokeWidth
        canvas.drawLine(startX, startY, endX.toFloat(), endY.toFloat(), paint)
    }

    private fun startTimeUpdates() {
        val timeUpdateHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                invalidate()


                sendEmptyMessageDelayed(0, 1000)
            }
        }
        timeUpdateHandler.sendEmptyMessage(0)
    }
}