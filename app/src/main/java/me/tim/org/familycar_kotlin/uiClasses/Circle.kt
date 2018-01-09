package me.tim.org.familycar_kotlin.uiClasses

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.graphics.RectF



/**
 * Created by Nekkyou on 16-11-2017.
 */
class Circle(context: Context?, attrs: AttributeSet?) :
        View(context, attrs) {

    private val START_ANGLE_POINT = -90f

    private val paint: Paint = Paint()
    private val textPaint: Paint = Paint()
    private var rect: RectF

    var angle: Float = 0f

    init {
        val strokeWidth = 40f
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth
        paint.color = Color.BLUE

        textPaint.strokeWidth = 0f
        textPaint.textSize = 100f
        textPaint.style = Paint.Style.STROKE
        textPaint.color = Color.BLACK
        textPaint.textAlign = Paint.Align.CENTER

        rect = RectF(strokeWidth, strokeWidth, 200 + strokeWidth, 200 + strokeWidth)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = Color.GRAY
        canvas.drawArc(rect, 0f, 360f, false, paint)
        paint.color = Color.BLUE
        canvas.drawArc(rect, START_ANGLE_POINT, angle, false, paint)

        canvas.drawText("${(angle/3.6).toInt()}", rect.centerX(), rect.centerY() + (textPaint.textSize / 3), textPaint)
    }
}