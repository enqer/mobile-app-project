package com.example.flextube.login

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.flextube.MainActivity
import com.example.flextube.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class LoginActivity : AppCompatActivity() {


    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val google: View = findViewById(R.id.google_area);
        val guest: View = findViewById(R.id.guest_area);
        val btn : Button = findViewById(R.id.test)
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(this,gso)

        google.setOnClickListener{
            signIn()

        }
        btn.setOnClickListener(){
            signOut()
        }


    }

    fun signIn() {
        val signInIntent: Intent = gsc.signInIntent
        startActivityForResult(signInIntent,1000)

    }
    fun getStatus(){
        val user: TextView = findViewById(R.id.user)
        val acct: GoogleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)
        if(acct!=null){
            val name: String = acct.email
            user.setText(name)
        }

    }

    fun signOut(){
        gsc.signOut()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1000 && resultCode == RESULT_OK){
            try {
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                task.getResult(ApiException::class.java)
                getStatus()
            } catch (e: ApiException) {
                Toast.makeText(this, "Something goes wrong: ${e.message}", Toast.LENGTH_SHORT).show()
            }

        }
    }
}