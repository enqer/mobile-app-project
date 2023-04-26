package com.example.flextube.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.flextube.R
import java.util.Locale


class SettingsActivity : AppCompatActivity() {

    lateinit var switch1: Switch
    private lateinit var sharedPreferences: SharedPreferences
    private val DARK_MODE = "darkMode"


    fun setLocale(activity: Activity, languageCode: String?) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources = activity.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val closeButtonIcon = findViewById<ImageView>(R.id.close_button)

        switch1 = findViewById(R.id.switch1)
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Odczytywanie zapisanego języka
        val savedLanguage = sharedPreferences.getString("language", "")

        // Initialize LinearLayout and add support for the click event
        val linearLayout2 = findViewById<LinearLayout>(R.id.linearLayout2) // language
        linearLayout2.setOnClickListener {
            val languages = arrayOf("English", "Polish") // Przykładowe dostępne języki
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Choose Language")
                .setItems(languages) { dialog, which ->
                    val selectedLanguage = languages[which]
                    if (selectedLanguage == "English"){
                        setLocale(this, "en")
                    }
                    if (selectedLanguage == "Polish"){
                        setLocale(this, "pl")
                    }
                    Toast.makeText(this, "Selected Language: $selectedLanguage", Toast.LENGTH_SHORT).show()

                    // Zapisywanie wybranego języka
                    val editor = sharedPreferences.edit()
                    editor.putString("language", selectedLanguage)
                    editor.apply()
                    dialog.dismiss() // Zamknięcie okna dialogowego po wyborze języka
                    // that's interesting
                    //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss() // Zamknięcie okna dialogowego po kliknięciu przycisku "Cancel"
                }
            alertDialogBuilder.create().show()
        }

        // Dodajemy obserwatora na zmiany wartości w SharedPreferences
        closeButtonIcon.setOnClickListener { // close button
            onBackPressed()
        }

        val linearLayout3 = findViewById<LinearLayout>(R.id.linearLayout3) // settings
        linearLayout3.setOnClickListener {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
        }

        val linearLayout5 = findViewById<LinearLayout>(R.id.linearLayout5) // help
        linearLayout5.setOnClickListener {
            Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show()
        }

        val linearLayout6 = findViewById<LinearLayout>(R.id.linearLayout6) // how it works
        linearLayout6.setOnClickListener {
            Toast.makeText(this, "How it works?", Toast.LENGTH_SHORT).show()
        }

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isDarkModeOn = sharedPreferences.getBoolean(DARK_MODE, false)
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            switch1.isChecked = true
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            switch1.isChecked = false
        }



        // Switch state change support
        switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // turn on dark mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPreferences.edit().putBoolean(DARK_MODE, true).apply()
            } else {
                // turn on light mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPreferences.edit().putBoolean(DARK_MODE, false).apply()
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}
