package com.example.connect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import android.view.*

class SettingPage : AppCompatActivity() {
    private var mGoogleSignInClient: GoogleSignInClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_page)
        setButtons()
    }

    private fun setButtons() {
        findViewById<View>(R.id.cl_signout_setting).setOnClickListener { signOut() }
    }

    private fun signOut() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        mGoogleSignInClient?.signOut()
            ?.addOnCompleteListener(this) {
                val intent = Intent(applicationContext, SigninPage::class.java)
                startActivity(intent)
            }
    }
}