package com.chatter.android.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.chatter.android.R
import com.chatter.android.fragment.FriendFragment
import com.chatter.android.fragment.NewChatFragment
import com.chatter.android.fragment.RequestFragment


class NewChatActivity : AppCompatActivity() {

    private lateinit var friendTextView: TextView
    private lateinit var requestTextView: TextView
    private lateinit var usersTextView: TextView
    private lateinit var navBackground: LinearLayout
    private lateinit var navBackgroundTextView: TextView
    private lateinit var relativeLayout: RelativeLayout

    private val newChatFragment: NewChatFragment = NewChatFragment()
    private val requestFragment: RequestFragment = RequestFragment()
    private val friendFragment: FriendFragment = FriendFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_chat)

        init()
        onLoad()
    }

    @SuppressLint("SetTextI18n")
    private fun init() {
        friendTextView = findViewById(R.id.friendTextView)
        requestTextView = findViewById(R.id.requestTextView)
        usersTextView = findViewById(R.id.usersTextView)
        navBackground = findViewById(R.id.navBackground)
        navBackgroundTextView = findViewById(R.id.navBackgroundTextView)
        relativeLayout = findViewById(R.id.relativeLayout)

        usersTextViewAction()
    }

    private fun onLoad() {
        setFragment(newChatFragment)

        friendTextView.setOnClickListener { friendTextViewAction() }
        requestTextView.setOnClickListener { requestTextViewAction() }
        usersTextView.setOnClickListener { usersTextViewAction() }
    }

    @SuppressLint("SetTextI18n")
    private fun friendTextViewAction() {
        setFragment(friendFragment)

        val lp = navBackground.layoutParams as RelativeLayout.LayoutParams
        lp.addRule(RelativeLayout.ALIGN_END, R.id.friendTextView)
        lp.removeRule(RelativeLayout.CENTER_VERTICAL)

        val transition = AutoTransition()
        transition.duration = 300
        TransitionManager.beginDelayedTransition(navBackground, transition)
        navBackground.layoutParams = lp

        navBackgroundTextView.text = "Friends"

        Thread {
            Thread.sleep(200)
            usersTextView.setTextColor(Color.parseColor("#707070"))
            requestTextView.setTextColor(Color.parseColor("#707070"))
            friendTextView.setTextColor(getColor(R.color.white))
        }.start()
    }

    @SuppressLint("SetTextI18n")
    private fun requestTextViewAction() {
        setFragment(requestFragment)

        val lp = navBackground.layoutParams as RelativeLayout.LayoutParams
        lp.addRule(RelativeLayout.ALIGN_END, R.id.requestTextView)
        lp.removeRule(RelativeLayout.CENTER_VERTICAL)

        val transition = AutoTransition()
        transition.duration = 300
        TransitionManager.beginDelayedTransition(navBackground, transition)
        navBackground.layoutParams = lp

        navBackgroundTextView.text = "Request"

        Thread {
            Thread.sleep(200)
            usersTextView.setTextColor(Color.parseColor("#707070"))
            friendTextView.setTextColor(Color.parseColor("#707070"))
            requestTextView.setTextColor(getColor(R.color.white))
        }.start()
    }

    @SuppressLint("SetTextI18n")
    private fun usersTextViewAction() {
        setFragment(newChatFragment)

        val lp = navBackground.layoutParams as RelativeLayout.LayoutParams
        lp.addRule(RelativeLayout.ALIGN_END, R.id.usersTextView)
        lp.removeRule(RelativeLayout.CENTER_VERTICAL)

        val transition = AutoTransition()
        transition.duration = 300
        TransitionManager.beginDelayedTransition(navBackground, transition)
        navBackground.layoutParams = lp

        navBackgroundTextView.text = "Users"

        Thread {
            Thread.sleep(200)
            friendTextView.setTextColor(Color.parseColor("#707070"))
            requestTextView.setTextColor(Color.parseColor("#707070"))
            usersTextView.setTextColor(getColor(R.color.white))
        }.start()
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, fragment)
        fragmentTransaction.commit()
    }
}