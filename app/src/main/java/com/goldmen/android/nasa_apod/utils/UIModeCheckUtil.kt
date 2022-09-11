package com.goldmen.android.nasa_apod.utils

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

fun modeCheck(mode: Int) {
    when (mode) {
        Configuration.UI_MODE_NIGHT_NO -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        Configuration.UI_MODE_NIGHT_YES -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}