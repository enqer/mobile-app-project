package com.example.flextube.login

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flextube.MainActivity
import com.example.flextube.api.ApiServices
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class LoginActivity : AppCompatActivity() {

    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.flextube.R.layout.activity_login)


        val google: View = findViewById(com.example.flextube.R.id.google_area)
        val guest: View = findViewById(com.example.flextube.R.id.guest_area)
        val serverClientId = getString(com.example.flextube.R.string.id_client)
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(serverClientId)
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(this, gso)


        google.setOnClickListener {
            signIn()

//              val signInIntent = Intent(this, MainActivity::class.java)
//            startActivity(signInIntent)

        }
        guest.setOnClickListener {
            val signInIntent = Intent(this, MainActivity::class.java)
            startActivity(signInIntent)
        }
//            val button: Button = findViewById(R.id.switch_to_main)
//            button.setOnClickListener {
//                val intent = Intent(this, SettingsActivity::class.java)
//                startActivity(intent)
//
//            }
    }

    fun signIn() {
        val signInIntent: Intent = gsc.signInIntent
        startActivityForResult(signInIntent, 1000)
        Log.d("LoginActivity", "Sign-in intent started")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("LoginActivity", "onActivityResult: requestCode=$requestCode, resultCode=$resultCode")
        if (requestCode == 1000) {
            try {
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken
                ApiServices.authToken = idToken ?: ""
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            } catch (e: ApiException) {
                Toast.makeText(this, "Something went wrong: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
