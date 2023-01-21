package com.chatter.android.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.chatter.android.R
import java.util.*

class WelcomeActivity : AppCompatActivity() {

    private lateinit var rightText: TextView
    private lateinit var registerButton: Button

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        init()
        onLoad()
    }

    private fun init() {
        rightText = findViewById(R.id.rightText)
        registerButton = findViewById(R.id.registerButton)
    }

    @SuppressLint("SetTextI18n")
    private fun onLoad() {
        rightText.text = "©All right reserved by ${getString(R.string.app_name)}. 2022-${
            Calendar.getInstance().get(Calendar.YEAR)
        }"

        registerButton.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
    }
}