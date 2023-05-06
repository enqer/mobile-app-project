package com.example.flextube.login

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.flextube.MainActivity
import com.example.flextube.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    companion object{
        private const val RC_SIGN_IN = 120
    }
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private var mAuthService: AuthorizationService? = null
    private var mStateManager: AuthStateManager? = null

//    lateinit var gso: GoogleSignInOptions
//    lateinit var gsc: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



//        mStateManager = AuthStateManager.getInstance(this)
//        mAuthService = AuthorizationService(this)
//
//        if (mStateManager?.current?.isAuthorized!!) {
//            Log.d("Auth", "Done")
//            mStateManager?.current?.performActionWithFreshTokens(
//                mAuthService!!
//            ) { accessToken, idToken, exception ->
//                Log.d("ACCESS TOKEN", accessToken.toString())
//                Log.d("ACCESS SCOPE", mStateManager?.current?.scope.toString())
//                ProfileTask().execute(accessToken)
//            }
//
//        }
//
//        val google: View = findViewById(R.id.google_area)
//        google.setOnClickListener {
//            if (mStateManager?.current?.isAuthorized!!){
//
//            }else {
//                val serviceConfig = AuthorizationServiceConfiguration(
//                    Uri.parse("https://accounts.google.com/o/oauth2/v2/auth"), // authorization endpoint
////                    Uri.parse("https://accounts.google.com/o/oauth2/v2/auth"), // authorization endpoint
//                    Uri.parse("https://www.googleapis.com/oauth2/v4/token") // token endpoint
//                )
//
//                val clientId = "460223798693-7pc3ftjusn904pa7b1faei2ci0r9ko45.apps.googleusercontent.com"
//                val redirectUri = Uri.parse("com.example.flextube:/oauth2callback")
//                val builder = AuthorizationRequest.Builder(
//                    serviceConfig,
//                    clientId,
//                    ResponseTypeValues.CODE,
//                    redirectUri
//                )
////                builder.setScopes("profile")
//
//                val authRequest = builder.build()
//                val authService = AuthorizationService(this)
//                val authIntent = authService.getAuthorizationRequestIntent(authRequest)
//                startActivityForResult(authIntent, RC_SIGN_IN)
//            }
//        }
//        val guestLogOut: View = findViewById(R.id.guest_area)
//        guestLogOut.setOnClickListener {
////            mStateManager.current.
//        }
//
//    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == RC_SIGN_IN) {
//            val resp = AuthorizationResponse.fromIntent(data!!)
//            val ex = AuthorizationException.fromIntent(data)
//
//            if (resp != null) {
//                mAuthService = AuthorizationService(this)
//                mStateManager?.updateAfterAuthorization(resp, ex)
//
//                mAuthService?.performTokenRequest(
//                    resp.createTokenExchangeRequest()
//                ) { resp, ex ->
//                    if (resp != null) {
//                        mStateManager?.updateAfterTokenResponse(resp, ex)
//                        Log.d("accessToken", resp.accessToken.toString())
//                        ProfileTask().execute(resp.accessToken)
//                    } else {
//                        // authorization failed, check ex for more details
//                    }
//                }
//
//                //Log.d("res",resp.accessToken)
//                // authorization completed
//            } else {
//                // authorization failed, check ex for more details
//            }
//            // ... process the response or exception ...
//        } else {
//            // ...
//        }
//        if (mStateManager?.current?.isAuthorized!!) {
//            Log.d("Auth", "Done")
//            mStateManager?.current?.performActionWithFreshTokens(
//                mAuthService!!
//            ) { accessToken, idToken, exception ->
//                ProfileTask().execute(accessToken)
//            }
//
//        }
//    }
//        mAuth.signOut()
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        if (user != null){
            val homeIntent = Intent(this, MainActivity::class.java)
//            startActivity(homeIntent)
        } else {
            val signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                .build()
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            googleSignInClient = GoogleSignIn.getClient(this, gso)

            mAuth = FirebaseAuth.getInstance()

        }
        val google: View = findViewById(R.id.google_area);
        google.setOnClickListener {
            signIn()
        }
        val guest: View = findViewById(R.id.guest_area)
        guest.setOnClickListener {
            mAuth.signOut()
        }
    }
    private fun signIn() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful){
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    Log.w("FirebaseAuthSuccess", account.id!!)
                    Log.w("FirebaseAuthSuccess", account.idToken!!)
                    account.serverAuthCode?.let { Log.w("FirebaseAuthSuccess", it) }
//                    updateUI(account)
                    firebaseAuthWithGoogle(account.idToken!!)
                }catch (e: ApiException){
                    Log.w("FirebaseAuthInSuccess", e)
                }
            }else{
                Log.w("FirebaseAuth", exception.toString())
            }

        }
//        user.authentication.do { authentication, error in
//            guard error == nil else { return }
//            guard let authentication = authentication else { return }
//
//            // Get the access token to attach it to a REST or gRPC request.
//            let accessToken = authentication.accessToken
//
//                    // Or, get an object that conforms to GTMFetcherAuthorizationProtocol for
//                    // use with GTMAppAuth and the Google APIs client library.
//                    let authorizer = authentication.fetcherAuthorizer()
//        }

    }

    private fun firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    Log.w("firebaseAuthWithGoogle", "success")
                    val user = mAuth.currentUser
                    Log.d("Current User", user?.displayName.toString())
                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
//                    finish()
                } else{
                    Log.w("firebaseAuthWithGoogle", "not success/", task.exception)
                }
            }
    }












//        val google: View = findViewById(R.id.google_area);

//        val guest: View = findViewById(R.id.guest_area);
//        gso =
//            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
//        gsc = GoogleSignIn.getClient(this, gso)
//
//        google.setOnClickListener {
//            signIn()
//
//        }
//        guest.setOnClickListener {
//            val signInIntent = Intent(this, MainActivity::class.java)
//            startActivity(signInIntent)
//        }
//            val button: Button = findViewById(R.id.switch_to_main)
//            button.setOnClickListener {
//                val intent = Intent(this, SettingsActivity::class.java)
//                startActivity(intent)
//
//            }


//    fun signIn() {
//        val signInIntent: Intent = gsc.signInIntent
//        startActivityForResult(signInIntent, 1000)
//
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 1000 && resultCode == RESULT_OK) {
//            try {
//                val task: Task<GoogleSignInAccount> =
//                    GoogleSignIn.getSignedInAccountFromIntent(data)
//                task.getResult(ApiException::class.java)
//            } catch (e: ApiException) {
//                Toast.makeText(this, "Something went wrong: ${e.message}", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
//    }

    inner class ProfileTask : AsyncTask<String?, Void, JSONObject>() {
        override fun doInBackground(vararg tokens: String?): JSONObject? {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://www.googleapis.com/oauth2/v3/userinfo")
                .addHeader("Authorization", String.format("Bearer %s", tokens[0]))
                .build()
            try {
                val response = client.newCall(request).execute()
                val jsonBody: String = response.body()!!.string()
                Log.i("LOG_TAG", String.format("User Info Response %s", jsonBody))
                return JSONObject(jsonBody)
            } catch (exception: Exception) {
                Log.w("LOG_TAG", exception)
            }
            return null
        }
        override fun onPostExecute(userInfo: JSONObject?) {
            if (userInfo != null) {
                val fullName = userInfo.optString("name", null)
                val imageUrl =
                    userInfo.optString("picture", null)

                if (!TextUtils.isEmpty(fullName)) {
                    Log.d("UserNAME", fullName)
                }

            }
        }
    }


}
