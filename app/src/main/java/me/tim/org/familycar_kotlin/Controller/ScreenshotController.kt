package me.tim.org.familycar_kotlin.Controller

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import java.util.*


/**
 * Created by Nekkyou on 6-11-2017.
 */
class ScreenshotController(val context: Context, val contentResolver: ContentResolver) {
    fun createScreenshot(bitmap: Bitmap) {
        val now = Date()
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)
        MediaStore.Images.Media.insertImage(contentResolver, bitmap, "ss $now", "family car ss")
    }
}