package com.example.flextube.interfaces

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

interface GoogleLogin {
    companion object{
        lateinit var gso: GoogleSignInOptions
        lateinit var gsc: GoogleSignInClient
    }
}