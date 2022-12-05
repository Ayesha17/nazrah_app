package com.nazrah.nazrahapp.utils

import android.os.Build
import android.text.Spanned
import androidx.core.text.HtmlCompat

object Html {
    fun fromHtml(source: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            HtmlCompat.fromHtml(source, HtmlCompat.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(source)
        }
    }
}