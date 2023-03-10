package com.chatter.android.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chatter.android.R
import com.chatter.android.activity.RegisterActivity

@Suppress("DEPRECATION")
class RegisterAllFragment : Fragment() {

    private lateinit var profileImage: ImageView
    private lateinit var fullName: TextView
    private lateinit var displayName: TextView
    private lateinit var phone: TextView
    private lateinit var country: TextView

    private lateinit var thiss: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_register_all, container, false)
        thiss = view.context

        init(view)
        onLoad()

        return view
    }

    private fun init(view: View) {
        profileImage = view.findViewById(R.id.profileImage)
        fullName = view.findViewById(R.id.fullName)
        displayName = view.findViewById(R.id.displayName)
        phone = view.findViewById(R.id.phone)
        country = view.findViewById(R.id.country)
    }

    private fun onLoad() {
        if (!RegisterActivity.user.imagePath) {
            Glide.with(thiss)
                .load(RegisterActivity.user.image.extras?.get("data"))
                .circleCrop()
                .into(profileImage)
        } else {
            Glide.with(thiss)
                .load(RegisterActivity.user.image.data)
                .circleCrop()
                .into(profileImage)
        }

        fullName.text = RegisterActivity.user.fullName
        displayName.text = RegisterActivity.user.displayName
        phone.text = RegisterActivity.user.phone
        country.text = RegisterActivity.user.country
    }
}