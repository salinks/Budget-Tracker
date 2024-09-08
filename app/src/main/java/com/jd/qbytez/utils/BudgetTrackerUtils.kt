package com.jd.qbytez.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

object BudgetTrackerUtils {
    fun getFormattedAmount(
        includeCurrencyCode: Boolean,
        input: Double,
        showDecimal: Boolean
    ): String {
        var input = input
        if (!showDecimal) {
            input = abs(input)
        }
        var outputValue = ""
        if (input == 0.0) {
            outputValue = if (showDecimal) {
                if (includeCurrencyCode) {
                    "₹ " + "0.00"
                } else {
                    "0.00"
                }
            } else {
                if (includeCurrencyCode) {
                    "₹ " + "0"
                } else {
                    "0"
                }
            }
        } else {
            var precision: NumberFormat = DecimalFormat(
                "###,###,###,###,###,###,###,###,###,###,###", DecimalFormatSymbols(
                    Locale.US
                )
            )
            if (showDecimal) {
                precision = DecimalFormat(
                    "###,###,###,###,###,###,###,###,###,###,###.00", DecimalFormatSymbols(
                        Locale.US
                    )
                )
            }
            outputValue = if (includeCurrencyCode) {
                if (precision.format(input).startsWith(".")) {
                    "₹ 0" + precision.format(input)
                } else {
                    "₹ " + precision.format(input)
                }
            } else {
                if (precision.format(input).startsWith(".")) {
                    "0" + precision.format(input)
                } else {
                    precision.format(input)
                }
            }
        }
        return outputValue
    }

    fun hideKeyboard(activity: Activity) {
        try {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getMonthYearName(monthName: String, yearNum: String): String {
        var mName = ""

        when (monthName) {
            "01" -> mName = "Jan-$yearNum"
            "02" -> mName = "Feb-$yearNum"
            "03" -> mName = "Mar-$yearNum"
            "04" -> mName = "Apr-$yearNum"
            "05" -> mName = "May-$yearNum"
            "06" -> mName = "Jun-$yearNum"
            "07" -> mName = "Jul-$yearNum"
            "08" -> mName = "Aug-$yearNum"
            "09" -> mName = "Sep-$yearNum"
            "10" -> mName = "Oct-$yearNum"
            "11" -> mName = "Nov-$yearNum"
            "12" -> mName = "Dec-$yearNum"

        }

        return mName;
    }
}
