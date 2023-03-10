package com.chatter.android.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.chatter.android.R
import com.chatter.android.activity.ChatActivity
import com.chatter.android.database.Database
import com.chatter.android.model.chat.ChatRegisterInDto
import com.chatter.android.model.chat.ChatRegisterOutDto
import com.chatter.android.model.message.MessageType
import com.chatter.android.model.message.SendMessageDto
import com.chatter.android.model.user.UserLoginOutDto
import com.chatter.android.retrofit.ChatController
import com.chatter.android.retrofit.FriendController
import com.chatter.android.retrofit.RetrofitService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendBottomListItemFragment(
    private val email: String
) : BottomSheetDialogFragment() {

    private lateinit var sendFirstMessage: Button
    private lateinit var deleteFriendButton: Button

    private lateinit var database: Database
    private lateinit var thiss: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View =
            inflater.inflate(R.layout.fragment_friend_bottom_list_item, container, false)
        thiss = view.context

        init(view)
        onLoad()

        return view
    }

    private fun init(view: View) {
        sendFirstMessage = view.findViewById(R.id.sendFirstMessageButton)
        deleteFriendButton = view.findViewById(R.id.deleteFriendButton)

        database = Database(thiss)
    }

    private fun onLoad() {
        checkSendFirstMessage()

        sendFirstMessage.setOnClickListener { sendFirstMessageAction() }
        deleteFriendButton.setOnClickListener { deleteFriend() }
    }

    private fun deleteFriend() {
        val retrofit = RetrofitService()
        val friendController: FriendController =
            retrofit.retrofit.create(FriendController::class.java)

        friendController.delete(email, database.readObject<UserLoginOutDto>("myInfo").email)
            .enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    Toast.makeText(
                        context,
                        context?.getString(R.string.user_removed_sucessfully),
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Toast.makeText(
                        context,
                        context?.getString(R.string.the_connection_to_the_server_was_lost),
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
    }

    private fun checkSendFirstMessage() {
        val retrofit = RetrofitService()
        val chatController: ChatController = retrofit.retrofit.create(ChatController::class.java)

        chatController.checkChat(database.readObject<UserLoginOutDto>("myInfo").email, email)
            .enqueue(object : Callback<Boolean> {

                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    sendFirstMessage.isVisible = response.body()!!
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Toast.makeText(
                        thiss,
                        thiss.getString(R.string.the_connection_to_the_server_was_lost),
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
    }

    private fun sendFirstMessageAction() {
        val retrofit = RetrofitService()
        val chatController: ChatController = retrofit.retrofit.create(ChatController::class.java)

        val myEmail = database.readObject<UserLoginOutDto>("myInfo").email
        val chatRegisterInDto = ChatRegisterInDto(myEmail, email)

        chatController.registerChat(chatRegisterInDto, myEmail)
            .enqueue(object : Callback<ChatRegisterOutDto> {
                override fun onResponse(
                    call: Call<ChatRegisterOutDto>,
                    response: Response<ChatRegisterOutDto>
                ) {
                    val myInfo = database.readObject<UserLoginOutDto>("myInfo")
                    val sendMessageDto = SendMessageDto(
                        myInfo.fullName.split(' ')[0] + " start new chat",
                        myInfo.email,
                        ByteArray(0),
                        MessageType.FIRST_MESSAGE
                    )

                    val chatId = response.body()!!.id

                    chatController.sendMessage(sendMessageDto, response.body()!!.id)
                        .enqueue(object : Callback<Boolean> {
                            override fun onResponse(
                                call: Call<Boolean>,
                                response: Response<Boolean>
                            ) {
                                // TODO: Delete this page and open a new one for redemptions
                                val intent = Intent(
                                    context,
                                    ChatActivity::class.java
                                )
                                intent.putExtra("chatId", chatId)
                                startActivity(intent)
                                (context as AppCompatActivity).finish()
//                                Toast.makeText(
//                                    thiss,
//                                    "Message send",
//                                    Toast.LENGTH_LONG
//                                ).show()
                            }

                            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                                Toast.makeText(
                                    thiss,
                                    thiss.getString(R.string.the_message_was_not_sent),
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        })
                }

                override fun onFailure(call: Call<ChatRegisterOutDto>, t: Throwable) {
                    Toast.makeText(
                        thiss,
                        thiss.getString(R.string.we_couldnt_make_the_conversation),
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
    }
}