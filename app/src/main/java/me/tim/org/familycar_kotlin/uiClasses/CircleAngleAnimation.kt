package me.tim.org.familycar_kotlin.uiClasses

import android.view.animation.Animation
import android.view.animation.Transformation

/**
 * Created by Nekkyou on 16-11-2017.
 */
class CircleAngleAnimation(val circle: Circle, val percent: Int): Animation() {
    val oldAngle = circle.angle

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        val totalAngle = percent * 3.6f
        val angle =  oldAngle + ((totalAngle - oldAngle) * interpolatedTime)

        circle.angle = angle
        circle.requestLayout()
    }
}