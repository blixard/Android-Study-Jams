package com.example.connect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import androidx.constraintlayout.widget.ConstraintLayout

class MenuScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_screen)
        setButtons()
    }

    private fun setButtons() {
        val clCreateroom = findViewById<ConstraintLayout>(R.id.cl_createroom)
        clCreateroom.setOnClickListener {
            val intent = Intent(applicationContext, CreateRoom::class.java)
            startActivity(intent)
        }
        val clJoinroom = findViewById<ConstraintLayout>(R.id.cl_joinroom)
        clJoinroom.setOnClickListener {
            val intent = Intent(applicationContext, JoinRoom::class.java)
            startActivity(intent)
        }
        val clSetting = findViewById<ConstraintLayout>(R.id.cl_setting)
        clSetting.setOnClickListener {
            val intent = Intent(applicationContext, SettingPage::class.java)
            startActivity(intent)
        }
        val clAbout = findViewById<ConstraintLayout>(R.id.cl_about_menu)
        clAbout.setOnClickListener {
            val intent = Intent(applicationContext, AboutPage::class.java)
            startActivity(intent)
        }
        val clSearch = findViewById<ConstraintLayout>(R.id.cl_search_menu)
        clSearch.setOnClickListener {
            val intent = Intent(applicationContext, searchRoom::class.java)
            startActivity(intent)
        }
    }
}