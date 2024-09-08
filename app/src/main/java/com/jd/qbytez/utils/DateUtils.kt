package com.jd.qbytez.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Locale

object DateUtils {
    fun oneFormatToAnother(date: String?, oldFormat: String?, newFormat: String?): String? {
        return try {
            val originalFormat = SimpleDateFormat(
                oldFormat,
                Locale.US
            )
            val targetFormat = SimpleDateFormat(
                newFormat,
                Locale.ENGLISH
            )
            val d = originalFormat.parse(date)
            targetFormat.format(d)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("oneForToAn Exception", e.toString())
            Log.e("oneForToAn Exception", newFormat!!)
            Log.e("oneForToAn Exception", oldFormat!!)
            null
        }
    }


}
