package com.example.flextube.settings

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesManager {
    private const val PREFERENCES_NAME = "MyPreferences"
    private const val LANGUAGE_PREF = "languagePref"

    private var sharedPreferences: SharedPreferences? = null

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun setSelectedLanguage(language: String) {
        sharedPreferences?.edit()?.putString(LANGUAGE_PREF, language)?.apply()
    }

    fun getSelectedLanguage(): String? {
        return sharedPreferences?.getString(LANGUAGE_PREF, null)
    }
}
