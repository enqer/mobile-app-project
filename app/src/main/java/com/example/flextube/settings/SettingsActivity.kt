package com.example.flextube.settings

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatTextView
import com.example.flextube.R
import java.util.Locale



class SettingsActivity : AppCompatActivity() {

    lateinit var switch1: Switch
    //lateinit var languageTv: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private val languageChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == "language") {
                val selectedLanguage = sharedPreferences.getString("language", "")
                updateLanguageTextView(selectedLanguage) // Aktualizujemy tekst w TextView
                updateLocale(selectedLanguage) // Aktualizujemy język na poziomie systemowym
            }
        }

    private fun updateLanguageTextView(language: String?) {
        val languageTv = findViewById<TextView>(R.id.language_TV)
        val languageKey = if (language == "Polski") "language_pl" else "language_en"
        val languageText = resources.getString(resources.getIdentifier(languageKey, "string", packageName), language)
        languageTv.text = languageText
    }

    private fun updateLocale(language: String?) {
        val locale = if (language == "Polski") {
            Locale("pl")
        } else {
            Locale("en")
        }
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Usuwamy obserwatora przy zamykaniu aktywności
        sharedPreferences.unregisterOnSharedPreferenceChangeListener { sharedPreferences, key -> }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val closeButtonIcon = findViewById<ImageView>(R.id.close_button)
        val languageTv = findViewById<TextView>(R.id.language_TV)

        switch1 = findViewById(R.id.switch1)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Odczytywanie zapisanego języka
        val savedLanguage = sharedPreferences.getString("language", "")
        updateLanguageTextView(savedLanguage)

        // Initialize LinearLayout and add support for the click event
        val linearLayout2 = findViewById<LinearLayout>(R.id.linearLayout2) // language
        linearLayout2.setOnClickListener {
            val languages = arrayOf("English", "Polski") // Przykładowe dostępne języki
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Choose Language")
                .setItems(languages) { dialog, which ->
                    val selectedLanguage = languages[which]
                    // Tutaj wykonaj akcję związana z wyborem języka
                    updateLanguageTextView(selectedLanguage) // Aktualizujemy tekst w TextView
                    updateLocale(selectedLanguage) // Aktualizujemy język na poziomie systemowym
                    Toast.makeText(this, "Selected Language: $selectedLanguage", Toast.LENGTH_SHORT).show()

                    // Zapisywanie wybranego języka
                    val editor = sharedPreferences.edit()
                    editor.putString("language", selectedLanguage)
                    editor.apply()
                    dialog.dismiss() // Zamknięcie okna dialogowego po wyborze języka
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss() // Zamknięcie okna dialogowego po kliknięciu przycisku "Cancel"
                }
            alertDialogBuilder.create().show()
        }

        // Dodajemy obserwatora na zmiany wartości w SharedPreferences
        sharedPreferences.registerOnSharedPreferenceChangeListener(languageChangeListener)

        closeButtonIcon.setOnClickListener { // close button
            // Here put the code to be executed when you click item
            Toast.makeText(this, "Close", Toast.LENGTH_SHORT).show()
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

        // Switch state change support
        switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // turn on dark mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                // turn on light mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

    }
}
