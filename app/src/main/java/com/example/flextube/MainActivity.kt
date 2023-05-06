package com.example.flextube

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.flextube.api.ApiServices
import com.example.flextube.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // tu będzie coś
        // albo nie
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // tests
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        Log.d("Current User", currentUser?.displayName.toString())
        Log.d("Current User", currentUser?.uid.toString())
        Log.d("Current User", currentUser?.email.toString())
        Log.d("Current User", currentUser?.photoUrl.toString())

//        val api = ApiServices.getRetrofitAuth()
//        val a: Call<String> = api.auth(login_hint = currentUser?.email.toString())
//        a.enqueue(object : Callback<String>{
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                Log.d("AUTH", "works")
//                if (response.isSuccessful){
//                    Log.d("AUTH", response.body().toString())
//                }
//            }
//
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                Log.d("AUTH", "not works")
//            }
//        })
        val useragent: String = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36"
//        val s = "https://accounts.google.com/o/oauth2/v2/auth?scope=email%20profile&response_type=code&redirect_uri=com.example.flextube%3A/oauth2redirect&client_id=460223798693-7pc3ftjusn904pa7b1faei2ci0r9ko45.apps.googleusercontent.com"
        val s = "https://accounts.google.com/o/oauth2/v2/auth?scope=email%20profile&response_type=code&redirect_uri=http://127.0.0.1:8080&client_id=460223798693-7pc3ftjusn904pa7b1faei2ci0r9ko45.apps.googleusercontent.com"
        val webView = WebView(this)
        webView.webChromeClient = WebChromeClient()
        webView.settings.allowFileAccess = true
        webView.settings.mediaPlaybackRequiresUserGesture=false
        webView.settings.pluginState = WebSettings.PluginState.ON
        webView.settings.pluginState = WebSettings.PluginState.ON_DEMAND
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.allowFileAccess = true
        webView.settings.allowContentAccess = true
        webView.settings.loadsImagesAutomatically = true
        webView.settings.javaScriptEnabled = true
        webView.settings.userAgentString = useragent
        webView.loadUrl("https://accounts.google.com/o/oauth2/v2/auth?scope=email%20profile&response_type=code&redirect_uri=com.example.flextube%3A/oauth2redirect&client_id=460223798693-7pc3ftjusn904pa7b1faei2ci0r9ko45.apps.googleusercontent.com")
        val int = Intent(Intent.ACTION_VIEW, Uri.parse(s))
//        webView.context.startActivity(int)
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setView(webView)
        alertDialogBuilder.create().show()



        //
//        onclick
        //mAuth.signOut()
        //intent to login
        //startac
        //finish()



        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_shorts, R.id.navigation_library
            )
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}