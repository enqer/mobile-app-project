package com.example.flextube.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.example.flextube.R

class SettingsActivity : AppCompatActivity() {
    lateinit var switch1: Switch
    //lateinit var name_TV: TextView
    //var name_TV = findViewById<TextView>(R.id.name_TV)
    //val personIcon = findViewById<ImageView>(R.id.person_icon)
    lateinit var closeButtonIcon: ImageView
    //lateinit var personIcon: ImageView
    lateinit var moonImage: ImageView
    lateinit var languageImage: ImageView
    lateinit var settingsImage: ImageView
    lateinit var questionImage: ImageView
    lateinit var informationImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Inicjalizacja elementów interfejsu użytkownika
        closeButtonIcon = findViewById(R.id.close_button)
        switch1 = findViewById(R.id.switch1)
        //personIcon = findViewById(R.id.person_icon)
        moonImage = findViewById(R.id.moon_image)
        languageImage = findViewById(R.id.language_image)
        settingsImage = findViewById(R.id.settings_image)
        questionImage = findViewById(R.id.question_mark_image)
        informationImage = findViewById(R.id.information_image)



        // Obsługa zmiany stanu Switcha
        switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Włącz tryb ciemny
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                closeButtonIcon.setColorFilter(resources.getColor(R.color.white))
                //personIcon.setColorFilter(resources.getColor(R.color.white))
                moonImage.setColorFilter(resources.getColor(R.color.white))
                languageImage.setColorFilter(resources.getColor(R.color.white))
                settingsImage.setColorFilter(resources.getColor(R.color.white))
                questionImage.setColorFilter(resources.getColor(R.color.white))
                informationImage.setColorFilter(resources.getColor(R.color.white))
            } else {
                // Włącz tryb jasny
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                closeButtonIcon.setColorFilter(resources.getColor(R.color.white))
                //personIcon.setColorFilter(resources.getColor(R.color.black))
                moonImage.setColorFilter(resources.getColor(R.color.black))
                languageImage.setColorFilter(resources.getColor(R.color.black))
                settingsImage.setColorFilter(resources.getColor(R.color.black))
                questionImage.setColorFilter(resources.getColor(R.color.black))
                informationImage.setColorFilter(resources.getColor(R.color.black))
            }
        }
    }
}
