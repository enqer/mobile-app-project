package com.example.flextube.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.flextube.R
import com.example.flextube.interfaces.GoogleLogin
import com.example.flextube.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.squareup.picasso.Picasso
import java.util.Locale


class SettingsActivity : AppCompatActivity() {

    //initialization of variables
    private var selectedLanguage: String? = null
    lateinit var switch1: Switch
    private val DARK_MODE_PREF = "darkModePref"
    private val LANGUAGE_PREF = "languagePref"

    // set selected language function
    fun setLocale(activity: Activity, languageCode: String?) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources = activity.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    // string to language code function
    fun getLanguageCode(language: String): String {
        return when (language) {
            "English" -> "en"
            "Polish" -> "pl"
            else -> "en" // default
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // initialization of variables
        val account = GoogleSignIn.getLastSignedInAccount(this)
        val signOut = findViewById<Button>(R.id.signOut)
        val logo = findViewById<ImageView>(R.id.person_icon)
        val author = findViewById<TextView>(R.id.name_TV)

        switch1 = findViewById(R.id.switch1)
        val closeButtonIcon = findViewById<ImageView>(R.id.close_button)
        val linearLayout2 = findViewById<LinearLayout>(R.id.linearLayout2) // language
        val linearLayout5 = findViewById<LinearLayout>(R.id.linearLayout5) // help
        val linearLayout6 = findViewById<LinearLayout>(R.id.linearLayout6) // how it works

        // shared preferences
        val darkModePrefs = getSharedPreferences(DARK_MODE_PREF, 0)
        val languagePrefs = getSharedPreferences(LANGUAGE_PREF, Context.MODE_PRIVATE)


        // set photo and person name
        Picasso.get().load(account.photoUrl.toString()).into(logo)
        author.setText(account.displayName)

        // sign out button handler
        signOut.setOnClickListener {
            GoogleLogin.gsc.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        // dark mode shared preferences
        val isDarkModeOn = darkModePrefs.getBoolean(DARK_MODE_PREF, false)
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            switch1.isChecked = true

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            switch1.isChecked = false
        }

        // save dark mode status
        val editorD = darkModePrefs.edit()
        editorD.putBoolean(DARK_MODE_PREF, isDarkModeOn)
        editorD.apply()

        // dark mode switch handler
        switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // turn on dark mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                darkModePrefs.edit().putBoolean(DARK_MODE_PREF, true).apply()
            } else {
                // turn on light mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                darkModePrefs.edit().putBoolean(DARK_MODE_PREF, false).apply()
            }
        }


        // language shared preferences
        selectedLanguage =
            languagePrefs.getString(LANGUAGE_PREF, "Polish") // default to English
        setLocale(this, selectedLanguage?.let { getLanguageCode(it) })


        // change language
        linearLayout2.setOnClickListener {
            val languages = arrayOf("English", "Polish")
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Choose Language")
                .setItems(languages) { dialog, which ->
                    selectedLanguage = languages[which]
                    setLocale(this, getLanguageCode(selectedLanguage!!)) // Set selected language
                    Toast.makeText(this, "Selected Language: $selectedLanguage", Toast.LENGTH_SHORT)
                        .show()

                    // Language save
                    //val editor = languagePrefs.edit()
                    //editor.putString(LANGUAGE_PREF, selectedLanguage)
                    //editor.apply()
                    SharedPreferencesManager.initialize(applicationContext)
                    SharedPreferencesManager.setSelectedLanguage(selectedLanguage!!)
                    Log.d(selectedLanguage, "selectedLanguage")

                    if (switch1.isChecked == true){
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }



                    dialog.dismiss() // close window
                    recreate()

//                    finish();
//                    startActivity(getIntent());

                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss() // close window at cancel button
                }
            alertDialogBuilder.create().show()
        }

        closeButtonIcon.setOnClickListener { // close button
            onBackPressed()
        }

        // help
        linearLayout5.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            if(selectedLanguage.equals("Polish")){
                builder.setMessage("Tutaj nie ma pomocy.")
            } else {
                builder.setMessage("There is no help.")
            }
            builder.setPositiveButton("OK", null)
            builder.show()
        }

        // how it works
        linearLayout6.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            if(selectedLanguage.equals("Polish")){
                builder.setMessage("Nie działa")
            } else {
                builder.setMessage("It doesn't")
            }
            builder.setPositiveButton("OK", null)
            builder.show()
        }
    }

    // back press button handler
    override fun onBackPressed() {
        super.onBackPressed()
    }

}