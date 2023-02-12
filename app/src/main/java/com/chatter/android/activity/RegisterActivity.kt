package com.chatter.android.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.chatter.android.R
import com.chatter.android.fragment.*
import com.chatter.android.model.Image
import com.chatter.android.model.UserRegisterInDto
import com.chatter.android.model.UserRegisterOutDto
import com.chatter.android.model.UserRegisterView
import com.chatter.android.retrofit.RetrofitService
import com.chatter.android.retrofit.UserController
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var dot1: LinearLayout
    private lateinit var dot2: LinearLayout
    private lateinit var dot3: LinearLayout
    private lateinit var dot4: LinearLayout

    private lateinit var dotText1: TextView
    private lateinit var dotText2: TextView
    private lateinit var dotText3: TextView
    private lateinit var dotText4: TextView

    private lateinit var registerButton: Button
    private lateinit var backButton: ImageView

    private var frame = 0

    companion object {
        @SuppressLint("StaticFieldLeak")
        val user: UserRegisterView = UserRegisterView()

        fun readField(field: TextInputLayout): String {
            return field.editText?.text.toString()
        }
    }

    private var fragments: List<Fragment> = listOf(
        RegisterEmailPasswordFragment(),
        RegisterFullDisplayNameFragment(),
        RegisterPhoneCountryBirthdayFragment(),
        RegisterPhotoFragment(),
        RegisterAllFragment()
    )

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, RegisterEmailPasswordFragment())
            .commitAllowingStateLoss()

        init()
        onLoad()
    }

    private fun init() {
        dot1 = findViewById(R.id.dot_1)
        dot2 = findViewById(R.id.dot_2)
        dot3 = findViewById(R.id.dot_3)
        dot4 = findViewById(R.id.dot_4)
        dotText1 = findViewById(R.id.dot_text_1)
        dotText2 = findViewById(R.id.dot_text_2)
        dotText3 = findViewById(R.id.dot_text_3)
        dotText4 = findViewById(R.id.dot_text_4)
        registerButton = findViewById(R.id.buttonRegister)
        backButton = findViewById(R.id.backButton)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun onLoad() {
        dot1.setOnClickListener {
            frame = 0
            updateDotTheme()
            loadFragment()
        }
        dot2.setOnClickListener {
            frame = 1
            updateDotTheme()
            loadFragment()
        }
        dot3.setOnClickListener {
            frame = 2
            updateDotTheme()
            loadFragment()
        }
        dot4.setOnClickListener {
            frame = 3
            updateDotTheme()
            loadFragment()
        }
        registerButton.setOnClickListener {

            if (frame == 4) {
                val retrofit: Retrofit = RetrofitService().retrofit

                val userController: UserController = retrofit.create(UserController::class.java)

                val userRegisterInDto = UserRegisterInDto(
                    user.fullName,
                    user.displayName,
                    user.phone,
                    user.country,
                    user.birthdayDate!!,
                    user.email,
                    user.password
                )

                userController.register(userRegisterInDto)
                    .enqueue(object : Callback<UserRegisterOutDto> {
                        @SuppressLint("Recycle", "Range")
                        override fun onResponse(
                            call: Call<UserRegisterOutDto>,
                            response: Response<UserRegisterOutDto>
                        ) {
                            if (response.code() == 200) {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    getString(R.string.registration_was_successful),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<UserRegisterOutDto>, t: Throwable) {
                            Toast.makeText(this@RegisterActivity, getString(R.string.registration_was_not_successful), Toast.LENGTH_LONG)
                                .show()
                        }

                    })

                val dataStr: String = Base64.getEncoder().encodeToString(user.imageData)

                userController.uploadProfileImage(Image(dataStr, user.email))
                    .enqueue(object : Callback<Boolean> {
                        override fun onResponse(
                            call: Call<Boolean>,
                            response: Response<Boolean>
                        ) {
                        }

                        override fun onFailure(call: Call<Boolean>, t: Throwable) {
                            Toast.makeText(
                                this@RegisterActivity,
                                getString(R.string.photo_failed_to_upload),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    })

                onBackPressed()
            }

            if (frame < 4) frame++

            updateDotTheme()
            loadFragment()
        }

        backButton.setOnClickListener { onBackPressed() }
    }

    private fun loadFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, fragments[frame])
            .commitAllowingStateLoss()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun updateDotTheme() {
        when (frame) {
            0 -> {
                dot1.background = getDrawable(R.drawable.button_register_active)
                dot2.background = getDrawable(R.drawable.button_register_inactive)
                dot3.background = getDrawable(R.drawable.button_register_inactive)
                dot4.background = getDrawable(R.drawable.button_register_inactive)

                dotText1.setTextColor(getColor(R.color.registerActive))
                dotText2.setTextColor(getColor(R.color.registerInactive))
                dotText3.setTextColor(getColor(R.color.registerInactive))
                dotText4.setTextColor(getColor(R.color.registerInactive))
            }
            1 -> {
                dot1.background = getDrawable(R.drawable.button_register_inactive)
                dot2.background = getDrawable(R.drawable.button_register_active)
                dot3.background = getDrawable(R.drawable.button_register_inactive)
                dot4.background = getDrawable(R.drawable.button_register_inactive)

                dotText1.setTextColor(getColor(R.color.registerInactive))
                dotText2.setTextColor(getColor(R.color.registerActive))
                dotText3.setTextColor(getColor(R.color.registerInactive))
                dotText4.setTextColor(getColor(R.color.registerInactive))
            }
            2 -> {
                dot1.background = getDrawable(R.drawable.button_register_inactive)
                dot2.background = getDrawable(R.drawable.button_register_inactive)
                dot3.background = getDrawable(R.drawable.button_register_active)
                dot4.background = getDrawable(R.drawable.button_register_inactive)

                dotText1.setTextColor(getColor(R.color.registerInactive))
                dotText2.setTextColor(getColor(R.color.registerInactive))
                dotText3.setTextColor(getColor(R.color.registerActive))
                dotText4.setTextColor(getColor(R.color.registerInactive))
            }
            3 -> {
                dot1.background = getDrawable(R.drawable.button_register_inactive)
                dot2.background = getDrawable(R.drawable.button_register_inactive)
                dot3.background = getDrawable(R.drawable.button_register_inactive)
                dot4.background = getDrawable(R.drawable.button_register_active)

                dotText1.setTextColor(getColor(R.color.registerInactive))
                dotText2.setTextColor(getColor(R.color.registerInactive))
                dotText3.setTextColor(getColor(R.color.registerInactive))
                dotText4.setTextColor(getColor(R.color.registerActive))
            }
        }

        if (frame == 4) {
            registerButton.background = getDrawable(R.drawable.button_full)
            registerButton.setTextColor(getColor(R.color.black))
            registerButton.text = getString(R.string.register)
        } else {
            registerButton.background = getDrawable(R.drawable.button_outline)
            registerButton.setTextColor(getColor(R.color.white))
            registerButton.text = getString(R.string.next)
        }

        checkDot()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun checkDot() {
        if (user.email.isNotEmpty() && checkEmail(user.email) && user.password.isNotEmpty() && user.password.length >= 6) {
            dot1.background = getDrawable(R.drawable.button_register_complete)
            dotText1.setTextColor(getColor(R.color.white))
        }

        if (user.fullName.isNotEmpty() && user.displayName.isNotEmpty()) {
            dot2.background = getDrawable(R.drawable.button_register_complete)
            dotText2.setTextColor(getColor(R.color.white))
        }

        if (user.phone.isNotEmpty() && checkPhone("+" + user.phone) && user.country.isNotEmpty() && user.birthdayDate != null) {
            dot3.background = getDrawable(R.drawable.button_register_complete)
            dotText3.setTextColor(getColor(R.color.white))
        }

        if (user.image != null) {
            dot4.background = getDrawable(R.drawable.button_register_complete)
            dotText4.setTextColor(getColor(R.color.white))
        }
    }

    private fun checkEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    private fun checkPhone(phone: String): Boolean {
        val phonePattern = "^[+][0-9]{10,13}\$"
        return phone.matches(phonePattern.toRegex())
    }
}