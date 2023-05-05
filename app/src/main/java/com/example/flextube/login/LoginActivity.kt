package com.example.flextube.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.flextube.MainActivity
import com.example.flextube.api.ApiServices
import com.example.flextube.auth.TokenResponse
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient
    //val webView: WebView = WebView(this)
    // val webView: WebView = WebView(requireNotNull(this).applicationContext)


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
//          signOut()
//              val signInIntent = Intent(this, MainActivity::class.java)
//            startActivity(signInIntent)

        }
        guest.setOnClickListener {
            signOut()
//            val signInIntent = Intent(this, MainActivity::class.java)
//            startActivity(signInIntent)
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
    fun signOut(){
        gsc.signOut()
    }
    fun auth(){
        val webView:WebView = WebView(applicationContext)
        val settings: WebSettings = webView.settings
        settings.userAgentString = "Mozilla/5.0 (Linux; Android 11; Pixel 4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Mobile Safari/537.36"
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://accounts.google.com/o/oauth2/v2/auth?client_id=469398138855-2e433poad55hgnvjhe2l7ljj2b09bkqg.apps.googleusercontent.com&redirect_uri=http://localhost:8080&response_type=code&scope=https://www.googleapis.com/auth/youtube")
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                Log.d("url", webView.url.toString())
                if (url.contains("code=")) {
                    // Wyciągamy kod autoryzacyjny z URL-a
                    val code = url.substringAfter("code=").substringBefore("&")
                    Log.d("kod autoryzacyjny", code.toString())
                    authToken(code)

                }
            }
        }
        val alertDialogBuilder = AlertDialog.Builder(this@LoginActivity)
        alertDialogBuilder.setTitle("Auto")
        alertDialogBuilder.setView(webView)
        alertDialogBuilder.create().show()
    }
    fun authToken(code:String){
        val apiService = ApiServices.getRetrofit2()
        val call = apiService.getToken(code=code)
        call.enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                Log.d("kod response",response.code().toString())
                Log.d("kod autoryzacyjny",code)
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("tak","git")
                    if (result != null) {
                        Log.d("token", result.accessToken.toString())
                        ApiServices.authToken = result.accessToken
                    }
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                Log.i("nie",t.message.toString())
            }
        })
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("LoginActivity", "onActivityResult: requestCode=$requestCode, resultCode=$resultCode")
        if (requestCode == 1000) {
            try {
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken
                val acc =account.serverAuthCode
                if (acc != null) {
                    Log.d("server code",acc)
                }
                auth()
            } catch (e: ApiException) {
                Toast.makeText(this, "Something went wrong: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
