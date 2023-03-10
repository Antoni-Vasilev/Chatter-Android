package com.chatter.android.activity

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chatter.android.R
import com.chatter.android.adapter.MessageAdapter
import com.chatter.android.database.Database
import com.chatter.android.model.chat.ChatMyChatsDto
import com.chatter.android.model.message.MessageInfo
import com.chatter.android.model.message.MessageType
import com.chatter.android.model.message.SendMessageDto
import com.chatter.android.model.user.UserLoginOutDto
import com.chatter.android.retrofit.ChatController
import com.chatter.android.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket


class ChatActivity : AppCompatActivity() {

    private lateinit var profileImage: ImageView
    private lateinit var fullName: TextView
    private lateinit var recycler: RecyclerView
    private lateinit var sendMessageButton: LottieAnimationView
    private lateinit var messageField: EditText

    private lateinit var database: Database
    private lateinit var messageAdapter: MessageAdapter
    private var messages: List<MessageInfo> = listOf()
    private lateinit var chatMyChatsDto: ChatMyChatsDto
    private var isThumbUpButton = true

    private lateinit var socket: Socket
    private lateinit var read: BufferedReader
    private lateinit var out: PrintWriter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        init()
        onLoad()
    }

    private fun init() {
        profileImage = findViewById(R.id.profileImage)
        fullName = findViewById(R.id.fullName)
        recycler = findViewById(R.id.recycler)
        sendMessageButton = findViewById(R.id.sendMessageButton)
        messageField = findViewById(R.id.messageField)

        database = Database(this)
    }

    private fun onLoad() {
        loadUserInfo()

        sendMessageButton.setOnClickListener {
            val retrofit = RetrofitService()
            val chatController: ChatController =
                retrofit.retrofit.create(ChatController::class.java)

            if (messageField.text.isNotEmpty()) {
                val sendMessageDto = SendMessageDto(
                    messageField.text.toString(),
                    database.readObject<UserLoginOutDto>("myInfo").email,
                    ByteArray(0),
                    MessageType.MESSAGE
                )

                chatController.sendMessage(sendMessageDto, chatMyChatsDto.id)
                    .enqueue(object : Callback<Boolean> {
                        override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                            Thread {
                                if (socket.isConnected) {
                                    out.println(chatMyChatsDto.id.toString() + " newMessage null")
                                }
                            }.start()
                        }

                        override fun onFailure(call: Call<Boolean>, t: Throwable) {
                            Toast.makeText(
                                this@ChatActivity,
                                getString(R.string.the_connection_to_the_server_was_lost),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    })
            } else {
                val sendMessageDto = SendMessageDto(
                    "",
                    database.readObject<UserLoginOutDto>("myInfo").email,
                    ByteArray(0),
                    MessageType.LIKE
                )

                chatController.sendMessage(sendMessageDto, chatMyChatsDto.id)
                    .enqueue(object : Callback<Boolean> {
                        override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                            Thread {
                                if (socket.isConnected) {
                                    out.println(chatMyChatsDto.id.toString() + " newMessage null")
                                }
                            }.start()
                        }

                        override fun onFailure(call: Call<Boolean>, t: Throwable) {
                            Toast.makeText(
                                this@ChatActivity,
                                getString(R.string.the_connection_to_the_server_was_lost),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    })
            }

            sendMessageButton.playAnimation()
        }

        messageField.addTextChangedListener {
            sendMessageButton.frame = 0
            val animationTime: Long = 600

            if (messageField.text.isEmpty() && !isThumbUpButton) {
                sendMessageButton.animate().rotation(0f).duration = animationTime
                sendMessageButton.speed = 1f

                Thread {
                    Thread.sleep((animationTime / 2))
                    runOnUiThread {
                        sendMessageButton.setAnimation(R.raw.thumb_up)
                    }
                }.start()

                isThumbUpButton = true
            } else if (isThumbUpButton) {
                sendMessageButton.animate().rotation(360f).duration = animationTime
                sendMessageButton.speed = 2f

                Thread {
                    Thread.sleep((animationTime / 2))
                    runOnUiThread {
                        sendMessageButton.setAnimation(R.raw.send)
                    }
                }.start()

                isThumbUpButton = false
            }
        }
    }

    private fun loadUserInfo() {
        val retrofit = RetrofitService()
        val chatController: ChatController = retrofit.retrofit.create(ChatController::class.java)

        chatController.getChatById(
            intent.getLongExtra("chatId", -1),
            database.readObject<UserLoginOutDto>("myInfo").email
        )
            .enqueue(object : Callback<ChatMyChatsDto> {
                override fun onResponse(
                    call: Call<ChatMyChatsDto>,
                    response: Response<ChatMyChatsDto>
                ) {
                    Glide.with(this@ChatActivity)
                        .load(retrofit.getUrl() + "/user/getProfileImage/" + response.body()!!.userInfo.email)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .circleCrop()
                        .into(profileImage)

                    fullName.text = response.body()!!.userInfo.fullName

                    chatMyChatsDto = response.body()!!
                    loadMessages()
                    connectToServer()
                }

                override fun onFailure(call: Call<ChatMyChatsDto>, t: Throwable) {
                    Toast.makeText(
                        this@ChatActivity,
                        getString(R.string.the_connection_to_the_server_was_lost),
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
    }

    private fun loadMessages() {
        messageAdapter = MessageAdapter(this, messages, chatMyChatsDto)

        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler.setHasFixedSize(true)
        recycler.adapter = messageAdapter

        val retrofit = RetrofitService()
        val chatController: ChatController = retrofit.retrofit.create(ChatController::class.java)

        chatController.getAllMessagesFromChat(intent.getLongExtra("chatId", -1))
            .enqueue(object : Callback<List<MessageInfo>> {

                override fun onResponse(
                    call: Call<List<MessageInfo>>,
                    response: Response<List<MessageInfo>>
                ) {
                    messages = response.body()!!
                    messageAdapter.update(messages)
                    recycler.scrollToPosition(messages.size - 1)
                }

                override fun onFailure(call: Call<List<MessageInfo>>, t: Throwable) {
                    Toast.makeText(
                        this@ChatActivity,
                        getString(R.string.the_connection_to_the_server_was_lost),
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
    }

    private fun loadNewMessages() {
        val retrofit = RetrofitService()
        val chatController: ChatController = retrofit.retrofit.create(ChatController::class.java)

        chatController.getAllNewMessages(chatMyChatsDto.id, messages[messages.size - 1].id)
            .enqueue(object : Callback<List<MessageInfo>> {
                override fun onResponse(
                    call: Call<List<MessageInfo>>,
                    response: Response<List<MessageInfo>>
                ) {
                    println("List get")
                    println(response.body())

                    if (response.body() != null) {
                        val list: MutableList<MessageInfo> = messages.toMutableList()
                        list.addAll(response.body()!!.toMutableList())
                        messages = list.toList()

                        messageAdapter.insert(messages, response.body()!!.size)
                        recycler.scrollToPosition(messages.size - 1)
                    }
                }

                override fun onFailure(call: Call<List<MessageInfo>>, t: Throwable) {
                    Toast.makeText(
                        this@ChatActivity,
                        getString(R.string.the_connection_to_the_server_was_lost),
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
    }

    private fun connectToServer() {
        Thread {
            socket =
                Socket(RetrofitService().getDatabaseAddress(), RetrofitService().getMessagesPort())
            read = BufferedReader(InputStreamReader(socket.getInputStream()))
            out = PrintWriter(socket.getOutputStream(), true)

            out.println(chatMyChatsDto.id.toString())
            out.println(database.readObject<UserLoginOutDto>("myInfo").email)
            if (read.readLine() != "done") return@Thread

            Thread {
                var isError = false
                while (!isError) {
                    try {
                        val message: String = read.readLine()
                        runOnUiThread {
                            if (message == "newMessage") loadNewMessages()
                        }
                    } catch (ignore: java.lang.Exception) {
                        isError = true
                        println("Error")
                    }
                }
            }.start()
        }.start()
    }

    override fun onPause() {
        Thread {
            out.println("disconnect")
        }.start()

        super.onPause()
    }
}