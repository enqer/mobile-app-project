package com.example.flextube.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.flextube.R

class SettingsActivity : AppCompatActivity() {

    lateinit var switch1: Switch

    fun closeClick(closeButtonIcon: ImageView){
        closeButtonIcon.setOnClickListener {
            // Here put the code to be executed when you click item
            Toast.makeText(this, "Close", Toast.LENGTH_SHORT).show()
        }
    }

    fun languageClick(){
        // Initialize LinearLayout and add support for the click event
        val linearLayout = findViewById<LinearLayout>(R.id.linearLayout2)
        linearLayout.setOnClickListener {
            // Here put the code to be executed when you click LinearLayout
            Toast.makeText(this, "Language", Toast.LENGTH_SHORT).show()
        }
    }

    fun settingsClick(){
        val linearLayout = findViewById<LinearLayout>(R.id.linearLayout3)
        linearLayout.setOnClickListener {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
        }
    }

    fun helpClick(){
        val linearLayout = findViewById<LinearLayout>(R.id.linearLayout5)
        linearLayout.setOnClickListener {
            Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show()
        }
    }

    fun howItWorksClick(){
        val linearLayout = findViewById<LinearLayout>(R.id.linearLayout6)
        linearLayout.setOnClickListener {
            Toast.makeText(this, "How it works?", Toast.LENGTH_SHORT).show()
        }
    }

    fun darkModeSwitch(){
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val closeButtonIcon = findViewById<ImageView>(R.id.close_button)
        switch1 = findViewById(R.id.switch1)

        closeClick(closeButtonIcon)
        languageClick()
        settingsClick()
        helpClick()
        howItWorksClick()

        darkModeSwitch()
        // notka: te funkcje jebnij to z powrotem tutaj
        // notka: dodać to gówno na więcej theme
        // notka: dodać inne kolory na tło całej aplikacji


    }
}
