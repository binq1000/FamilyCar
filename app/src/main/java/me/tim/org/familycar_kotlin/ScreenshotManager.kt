package me.tim.org.familycar_kotlin

import android.content.Context
import android.graphics.Bitmap
import java.util.*
import android.R.attr.bitmap
import android.content.ContentResolver
import android.provider.MediaStore


/**
 * Created by Nekkyou on 6-11-2017.
 */
class ScreenshotManager(val context: Context, val contentResolver: ContentResolver) {
    fun createScreenshot(bitmap: Bitmap) {
        val now = Date()
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)
        MediaStore.Images.Media.insertImage(contentResolver, bitmap, "ss $now", "family car ss")
    }
}