package com.example.flextube

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle


import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.flextube.api.ApiServices
import com.example.flextube.auth.TokenResponse
import com.example.flextube.databinding.ActivityMainBinding
import com.example.flextube.interfaces.GoogleLogin


import com.example.flextube.settings.SettingsActivity

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

import com.example.flextube.ui.home.HomeFragment


import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE)

        val acTOken = sharedPreferences.getString("access_token","error")
        val rfrTOken = sharedPreferences.getString("refresh_token","1//0c1e_0QNdd-TwCgYIARAAGAwSNwF-L9IrnjFQx4c_cx4sxjalaO8T-ZQAFWY-eZliyBchkEwNOvGreWjuL1XQZfuI4msGFEOLRcg")
        if (rfrTOken != null) {
            refreshToken(rfrTOken)
        }

        val account = GoogleSignIn.getLastSignedInAccount(this)

        val btnToSetting = binding.toSettings
        Picasso.get().load(account.photoUrl.toString()).into(btnToSetting)
        btnToSetting.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_shorts, R.id.navigation_library
            )
        )



        // searching videos by words
        val searchQuery = binding.searchQ
        searchQuery.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (p1 == EditorInfo.IME_ACTION_SEARCH){
                    val query: String = searchQuery.text.toString()
                    Log.i("Szukane hasło",query)

                    val bundle = Bundle()
                    bundle.putString("q", query)
                    navController.navigate(R.id.navigation_home,bundle)
//                    finish()
                    return true
                }
                return false
            }
        })
        // changing view to home on logo
        val logoApp = binding.logoApp
        logoApp.setOnClickListener {
            searchQuery.text.clear()
            navController.navigate(R.id.navigation_home)
        }


//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    private fun refreshToken(refresh_token: String){
        val apiService = ApiServices.getClient()
        val call = apiService.refreshToken(refreshToken = refresh_token)
        call.enqueue(object: Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if(response.isSuccessful){
                    val result = response.body()
                    if(result!=null){
                        //Log.d("Refresh Token",result.refreshToken)
                        Log.d("Access Token",result.accessToken)
                        val editor = sharedPreferences.edit()
                        editor.putString("access_token", result.accessToken)
                        editor.putString("refresh_token", result.refreshToken)
                        editor.apply()
                        GoogleLogin.access_token = result.accessToken
                       // GoogleLogin.refresh_token = result.refreshToken
                    }
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

}