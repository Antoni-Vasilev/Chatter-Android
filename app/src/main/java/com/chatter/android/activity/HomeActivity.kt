package com.chatter.android.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chatter.android.R
import com.chatter.android.adapter.ChatAdapter
import com.chatter.android.database.Database
import com.chatter.android.model.chat.ChatMyChatsDto
import com.chatter.android.model.user.UserLoginOutDto
import com.chatter.android.retrofit.ChatController
import com.chatter.android.retrofit.RetrofitService
import com.chatter.android.retrofit.UserController
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var newChatButton: ExtendedFloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchField: EditText
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var profileImage: ImageView

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chats: List<ChatMyChatsDto>
    private lateinit var database: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        init()
        onLoad()
        loadData()
    }

    private fun init() {
        newChatButton = findViewById(R.id.newChatButton)
        recyclerView = findViewById(R.id.recycler)
        searchField = findViewById(R.id.searchField)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        profileImage = findViewById(R.id.profileImage)

        chats = listOf()
        chatAdapter = ChatAdapter(this, chats)
        database = Database(this)
    }

    private fun onLoad() {
        newChatButton.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    NewChatActivity::class.java
                )
            )
        }

        recyclerView.adapter = chatAdapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        searchField.addTextChangedListener { loadData() }

        swipeRefresh.setOnRefreshListener { loadData() }

        loadProfileImage()
    }

    private fun loadProfileImage() {
        Glide.with(this)
            .load(
                RetrofitService().getUrl() + "/user/getProfileImage/" + database.readObject<UserLoginOutDto>(
                    "myInfo"
                ).email
            )
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .into(profileImage)
    }

    private fun loadData() {
        val retrofit = RetrofitService()
        val chatController: ChatController = retrofit.retrofit.create(ChatController::class.java)

        chatController.myChats(
            database.readObject<UserLoginOutDto>("myInfo").email,
            searchField.text.toString()
        )
            .enqueue(object : Callback<List<ChatMyChatsDto>> {

                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<List<ChatMyChatsDto>>,
                    response: Response<List<ChatMyChatsDto>>
                ) {
                    if (response.body() == null) return
                    chats = response.body()!!
                    chats = chats.sortedWith(compareBy { it.lastMessage?.sendDate }).reversed()
                    chatAdapter.updateData(chats)
                }

                override fun onFailure(call: Call<List<ChatMyChatsDto>>, t: Throwable) {
                }
            })
        swipeRefresh.isRefreshing = false

        updateStatus()
    }

    private fun updateStatus() {
        Thread {
            while (true) {
                val retrofit = RetrofitService()
                val userController: UserController =
                    retrofit.retrofit.create(UserController::class.java)

                userController.lastOpen(database.readObject<UserLoginOutDto>("myInfo").email)
                    .enqueue(object : Callback<Boolean> {
                        override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                        }

                        override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        }

                    })

                Thread.sleep(15000)
            }
        }.start()
    }
}