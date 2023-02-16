package com.chatter.android.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chatter.android.R
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class HomeActivity : AppCompatActivity() {

    private lateinit var newChatButton: ExtendedFloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        init()
        onLoad()
    }

    private fun init() {
        newChatButton = findViewById(R.id.newChatButton)
    }

    private fun onLoad() {
        newChatButton.setOnClickListener { startActivity(Intent(this, NewChatActivity::class.java)) }
    }
}