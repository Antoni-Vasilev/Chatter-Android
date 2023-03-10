package com.chatter.android.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chatter.android.R
import com.chatter.android.database.Database
import com.chatter.android.model.friendRequest.FriendRequestRegisterInDto
import com.chatter.android.model.friendRequest.FriendRequestRegisterOutDto
import com.chatter.android.model.user.UserInfoDto
import com.chatter.android.model.user.UserLoginOutDto
import com.chatter.android.retrofit.FriendRequestController
import com.chatter.android.retrofit.RetrofitService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserBottomInfoFragment(private val userInfoDto: UserInfoDto) : BottomSheetDialogFragment() {

    private lateinit var profileImage: ImageView
    private lateinit var fullName: TextView
    private lateinit var displayName: TextView
    private lateinit var displayNameCode: TextView
    private lateinit var sendingAFriendRequestButton: Button

    private lateinit var thiss: Context
    private lateinit var database: Database

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_user_bottom_info, container, false)
        thiss = view.context

        init(view)
        onLoad()

        checkSendButton()

        return view
    }

    private fun init(view: View) {
        profileImage = view.findViewById(R.id.profileImage)
        fullName = view.findViewById(R.id.fullName)
        displayName = view.findViewById(R.id.displayName)
        displayNameCode = view.findViewById(R.id.displayNameCode)
        sendingAFriendRequestButton = view.findViewById(R.id.sendAFriendRequestButton)

        database = Database(thiss)
    }

    private fun onLoad() {
        fullName.text = userInfoDto.fullName
        displayName.text = userInfoDto.displayName
        displayNameCode.text = userInfoDto.displayNameCode

        Glide.with(thiss)
            .load(RetrofitService().getUrl() + "/user/getProfileImage/" + userInfoDto.email)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .into(profileImage)

        sendingAFriendRequestButton.setOnClickListener {
            val retrofit = RetrofitService()
            val friendRequestController: FriendRequestController =
                retrofit.retrofit.create(FriendRequestController::class.java)

            val friendRequestRegisterInDto = FriendRequestRegisterInDto(
                database.readObject<UserLoginOutDto>("myInfo").email,
                userInfoDto.email
            )

            friendRequestController.registerRequest(friendRequestRegisterInDto)
                .enqueue(object : Callback<FriendRequestRegisterOutDto> {
                    override fun onResponse(
                        call: Call<FriendRequestRegisterOutDto>,
                        response: Response<FriendRequestRegisterOutDto>
                    ) {
                        if (response.code() == 200) {
                            Toast.makeText(
                                thiss,
                                getString(R.string.the_request_was_sent_successfully),
                                Toast.LENGTH_LONG
                            ).show()
                            sendingAFriendRequestButton.text =
                                getString(R.string.the_request_has_been_sent)
                        } else if (response.code() == 400) {
                            Toast.makeText(
                                thiss,
                                getString(R.string.the_request_was_removed),
                                Toast.LENGTH_LONG
                            ).show()
                            sendingAFriendRequestButton.text =
                                getString(R.string.send_a_friend_request)
                        }
                    }

                    override fun onFailure(call: Call<FriendRequestRegisterOutDto>, t: Throwable) {
                        Toast.makeText(
                            thiss,
                            getString(R.string.the_request_could_not_be_sent),
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                })
        }
    }

    private fun checkSendButton() {
        val retrofit = RetrofitService()
        val friendRequestController: FriendRequestController =
            retrofit.retrofit.create(FriendRequestController::class.java)

        val friendRequestRegisterInDto = FriendRequestRegisterInDto(
            database.readObject<UserLoginOutDto>("myInfo").email,
            userInfoDto.email
        )

        friendRequestController.checkRequest(friendRequestRegisterInDto)
            .enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (response.code() == 200) {
                        if (!response.body()!!) {
                            sendingAFriendRequestButton.text =
                                getString(R.string.the_request_has_been_sent)
                        } else {
                            sendingAFriendRequestButton.text =
                                getString(R.string.send_a_friend_request)
                        }
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                }
            })
    }
}