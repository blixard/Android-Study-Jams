package com.example.connect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.content.SharedPreferences
import android.util.Log
import kotlin.math.log


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)



        val prefs = getSharedPreferences("user_state", MODE_PRIVATE)
        val login = prefs.getBoolean("login", false)

        Log.d("ntr", "onCreate: login: $login" )
        if (!login){
            val editor = getSharedPreferences("user_state", MODE_PRIVATE).edit()
            editor.putBoolean("login", true)
            editor.apply()
            Handler().postDelayed({
                val intent = Intent(this, AboutPage::class.java)
                startActivity(intent)
                finish()
            }, 3333)
        }
        else{
            Handler().postDelayed({
                val intent = Intent(this, SigninPage::class.java)
                startActivity(intent)
                finish()
            }, 3333)
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        window.decorView.apply {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        supportActionBar?.hide()
    }
}