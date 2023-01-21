package com.chatter.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.chatter.android.R
import com.chatter.android.activity.RegisterActivity
import com.google.android.material.textfield.TextInputLayout

class RegisterEmailPasswordFragment : Fragment() {

    private lateinit var emailField: TextInputLayout
    private lateinit var passwordField: TextInputLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View =
            inflater.inflate(R.layout.fragment_register_email_password, container, false)

        init(view)
        onLoad()

        return view
    }

    private fun init(view: View) {
        emailField = view.findViewById(R.id.emailField)
        passwordField = view.findViewById(R.id.passwordField)

        emailField.editText?.setText(RegisterActivity.user.email)
        passwordField.editText?.setText(RegisterActivity.user.password)
    }

    private fun onLoad() {
        emailField.editText?.addTextChangedListener {
            RegisterActivity.user.email = RegisterActivity.readField(emailField)
        }

        passwordField.editText?.addTextChangedListener {
            RegisterActivity.user.password = RegisterActivity.readField(passwordField)
        }
    }
}