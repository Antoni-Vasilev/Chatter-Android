package com.chatter.android.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chatter.android.R
import com.chatter.android.database.Database
import com.chatter.android.model.UserLoginInDto
import com.chatter.android.model.UserLoginOutDto
import com.chatter.android.retrofit.RetrofitService
import com.chatter.android.retrofit.UserController
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var emailField: TextInputLayout
    private lateinit var passwordField: TextInputLayout
    private lateinit var loginButton: Button

    private lateinit var database: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
        onLoad()
    }

    private fun init() {
        database = Database(this)

        backButton = findViewById(R.id.backButton)
        emailField = findViewById(R.id.emailField)
        passwordField = findViewById(R.id.passwordField)
        loginButton = findViewById(R.id.buttonLogin)
    }

    private fun onLoad() {
        backButton.setOnClickListener { onBackPressed() }

        loginButton.setOnClickListener {
            val email: String = getFieldValue(emailField)
            val password: String = getFieldValue(passwordField)

            val retrofit = RetrofitService().retrofit
            val userController: UserController = retrofit.create(UserController::class.java)

            val data = UserLoginInDto(email, password)

            userController.login(data)
                .enqueue(object : Callback<UserLoginOutDto> {

                    override fun onResponse(
                        call: Call<UserLoginOutDto>,
                        response: Response<UserLoginOutDto>
                    ) {
                        if (response.code() == 200) {
                            database.saveObject(response.body()!!, "myInfo")
                            Toast.makeText(
                                this@LoginActivity,
                                getString(R.string.you_have_successfully_logged_in),
                                Toast.LENGTH_LONG
                            ).show()
                            database.saveBoolean(true, Database.isFirstLogin)
                            startActivity(Intent(this@LoginActivity, WelcomeActivity::class.java))
                            finishAffinity()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                getString(R.string.login_failed),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<UserLoginOutDto>, t: Throwable) {
                        Toast.makeText(
                            this@LoginActivity,
                            getString(R.string.login_failed),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                })
        }
    }

    private fun getFieldValue(field: TextInputLayout): String {
        return field.editText?.text.toString().trim()
    }
}