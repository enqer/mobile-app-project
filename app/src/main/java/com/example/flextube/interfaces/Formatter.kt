package com.example.flextube.interfaces

import android.content.Context
import com.example.flextube.R

interface Formatter {
    companion object {
        public fun formatNumber(number: String?, baseContext: Context): String {
            if (number == null)
                return "-1"
            val suffixes = listOf(
                "",
                baseContext.resources.getString(R.string.num1000),
                baseContext.resources.getString(R.string.num1000000),
                baseContext.resources.getString(R.string.num1000000000)
            )
            var i = 0
            var n = number.toDouble()
            while (n >= 1000 && i < suffixes.size - 1) {
                n /= 1000
                i++
            }
            val formattedNumber = "%.1f".format(n)
            return if (formattedNumber.endsWith(".0")) {
                formattedNumber.substringBefore(".") + suffixes[i]
            } else {
                "$formattedNumber ${suffixes[i]}"
            }
        }
    }
}