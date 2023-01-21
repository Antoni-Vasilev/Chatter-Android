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

class RegisterFullDisplayNameFragment : Fragment() {

    private lateinit var fullName: TextInputLayout
    private lateinit var displayName: TextInputLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View =
            inflater.inflate(R.layout.fragment_register_full_display_name, container, false)

        init(view)
        onLoad()

        return view
    }

    private fun init(view: View) {
        fullName = view.findViewById(R.id.fullNameField)
        displayName = view.findViewById(R.id.displayNameField)

        fullName.editText?.setText(RegisterActivity.user.fullName)
        displayName.editText?.setText(RegisterActivity.user.displayName)
    }

    private fun onLoad() {
        fullName.editText?.addTextChangedListener {
            RegisterActivity.user.fullName = RegisterActivity.readField(fullName)
        }

        displayName.editText?.addTextChangedListener {
            RegisterActivity.user.displayName = RegisterActivity.readField(displayName)
        }
    }
}