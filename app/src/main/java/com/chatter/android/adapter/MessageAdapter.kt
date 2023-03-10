package com.chatter.android.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chatter.android.R
import com.chatter.android.model.chat.ChatMyChatsDto
import com.chatter.android.model.message.MessageInfo
import com.chatter.android.model.message.MessageType
import com.chatter.android.retrofit.RetrofitService

@Suppress("DEPRECATION")
class MessageAdapter(
    private val context: Context,
    private var messages: List<MessageInfo>,
    private val chatMyChatsDto: ChatMyChatsDto
) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private var position: Int = 0

    class ViewHolder(
        itemView: View,
        position: Int,
        messages: List<MessageInfo>,
        chatMyChatsDto: ChatMyChatsDto
    ) : RecyclerView.ViewHolder(itemView) {

        // First message
        lateinit var profileImageFirstMessage: ImageView
        lateinit var fullNameFirstMessage: TextView
        lateinit var messageFirstMessage: TextView

        // Message right
        lateinit var messageMessageRight: TextView
        lateinit var timeMessageRight: TextView
        lateinit var backgroundMessageRight: LinearLayout
        lateinit var backgroundStyleMessageRight: LinearLayout

        // Message left
        lateinit var profileImageMessageLeft: ImageView
        lateinit var messageMessageLeft: TextView
        lateinit var timeMessageLeft: TextView
        lateinit var backgroundMessageLeft: LinearLayout
        lateinit var backgroundStyleMessageLeft: LinearLayout

        // Like right
        lateinit var animationLikeRight: LottieAnimationView
        lateinit var timeLikeRight: TextView

        // Like left
        lateinit var profileImageLikeLeft: ImageView
        lateinit var animationLikeLeft: LottieAnimationView
        lateinit var timeLikeLeft: TextView

        init {
            if (messages[position].type == MessageType.FIRST_MESSAGE) {
                profileImageFirstMessage = itemView.findViewById(R.id.profileImage)
                fullNameFirstMessage = itemView.findViewById(R.id.fullName)
                messageFirstMessage = itemView.findViewById(R.id.message)
            } else if (messages[position].type == MessageType.MESSAGE) {
                if (messages[position].sender.email != chatMyChatsDto.userInfo.email) {
                    messageMessageRight = itemView.findViewById(R.id.message)
                    timeMessageRight = itemView.findViewById(R.id.time)
                    backgroundMessageRight = itemView.findViewById(R.id.background)
                    backgroundStyleMessageRight = itemView.findViewById(R.id.background_style)
                } else {
                    profileImageMessageLeft = itemView.findViewById(R.id.profileImage)
                    messageMessageLeft = itemView.findViewById(R.id.message)
                    timeMessageLeft = itemView.findViewById(R.id.time)
                    backgroundMessageLeft = itemView.findViewById(R.id.background)
                    backgroundStyleMessageLeft = itemView.findViewById(R.id.background_style)
                }
            } else if (messages[position].type == MessageType.LIKE) {
                if (messages[position].sender.email != chatMyChatsDto.userInfo.email) {
                    animationLikeRight = itemView.findViewById(R.id.animation)
                    timeLikeRight = itemView.findViewById(R.id.time)
                } else {
                    profileImageLikeLeft = itemView.findViewById(R.id.profileImage)
                    animationLikeLeft = itemView.findViewById(R.id.animation)
                    timeLikeLeft = itemView.findViewById(R.id.time)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(viewType, parent, false)
        return ViewHolder(view, position, messages, chatMyChatsDto)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message: MessageInfo = messages[position]

        if (message.type == MessageType.FIRST_MESSAGE) { // ********************     First Message     *******************
            holder.fullNameFirstMessage.text = chatMyChatsDto.userInfo.fullName
            holder.messageFirstMessage.text =
                "${message.message} • ${fixTime(message.sendDate.hours - (message.sendDate.timezoneOffset / 60))}:${
                    fixTime(
                        message.sendDate.minutes
                    )
                }"

            Glide.with(context)
                .load(RetrofitService().getUrl() + "/user/getProfileImage/" + chatMyChatsDto.userInfo.email)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop()
                .into(holder.profileImageFirstMessage)
        } else if (message.type == MessageType.MESSAGE) { // ********************     Message     *******************
            if (message.sender.email != chatMyChatsDto.userInfo.email) { // ********************     Message right     *******************
                holder.messageMessageRight.text = message.message
                holder.timeMessageRight.text =
                    "${fixTime(message.sendDate.hours - (message.sendDate.timezoneOffset / 60))}:${
                        fixTime(
                            message.sendDate.minutes
                        )
                    }"

                var messagePositionType = MessagePositionType.ONLY

                if (position - 1 >= 0 && message.sender.email == messages[position - 1].sender.email
                    && messages[position - 1].type == MessageType.MESSAGE
                ) {
                    holder.backgroundMessageRight.setPadding(10, 4, 10, 4)
                    messagePositionType = MessagePositionType.LAST
                }

                if (position + 1 < messages.size && message.sender.email == messages[position + 1].sender.email
                    && messages[position + 1].type == MessageType.MESSAGE
                ) {
                    holder.backgroundMessageRight.setPadding(10, 4, 10, 4)
                    messagePositionType = MessagePositionType.FIRST
                }

                if (position - 1 >= 0 && position + 1 < messages.size
                    && messages[position - 1].type == MessageType.MESSAGE
                    && messages[position + 1].type == MessageType.MESSAGE
                ) {
                    if (message.sender.email == messages[position - 1].sender.email
                        && message.sender.email == messages[position + 1].sender.email
                    ) {
                        messagePositionType = MessagePositionType.CENTER
                    }
                }

                when (messagePositionType) {
                    MessagePositionType.FIRST -> holder.backgroundStyleMessageRight.background =
                        context.getDrawable(R.drawable.message_right_first_bg)
                    MessagePositionType.CENTER -> holder.backgroundStyleMessageRight.background =
                        context.getDrawable(R.drawable.message_right_center_bg)
                    MessagePositionType.LAST -> holder.backgroundStyleMessageRight.background =
                        context.getDrawable(R.drawable.message_right_last_bg)
                    else -> {}
                }
            } else { // ********************     Message left     *******************
                holder.messageMessageLeft.text = message.message
                holder.timeMessageLeft.text =
                    "${fixTime(message.sendDate.hours - (message.sendDate.timezoneOffset / 60))}:${
                        fixTime(
                            message.sendDate.minutes
                        )
                    }"

                var messagePositionType = MessagePositionType.ONLY

                if (position - 1 >= 0 && message.sender.email == messages[position - 1].sender.email
                    && messages[position - 1].type == MessageType.MESSAGE
                ) {
                    holder.backgroundMessageLeft.setPadding(10, 4, 10, 4)
                    messagePositionType = MessagePositionType.LAST
                }

                if (position + 1 < messages.size && message.sender.email == messages[position + 1].sender.email
                    && messages[position + 1].type == MessageType.MESSAGE
                ) {
                    holder.backgroundMessageLeft.setPadding(10, 4, 10, 4)
                    messagePositionType = MessagePositionType.FIRST
                }

                if (position - 1 >= 0 && position + 1 < messages.size
                    && messages[position - 1].type == MessageType.MESSAGE
                    && messages[position + 1].type == MessageType.MESSAGE
                ) {
                    if (message.sender.email == messages[position - 1].sender.email
                        && message.sender.email == messages[position + 1].sender.email
                    ) {
                        messagePositionType = MessagePositionType.CENTER
                    }
                }

                when (messagePositionType) {
                    MessagePositionType.FIRST -> holder.backgroundStyleMessageLeft.background =
                        context.getDrawable(R.drawable.message_left_first_bg)
                    MessagePositionType.CENTER -> holder.backgroundStyleMessageLeft.background =
                        context.getDrawable(R.drawable.message_left_center_bg)
                    MessagePositionType.LAST -> holder.backgroundStyleMessageLeft.background =
                        context.getDrawable(R.drawable.message_left_last_bg)
                    else -> {}
                }

                if (position + 1 < messages.size && message.sender.email != messages[position + 1].sender.email)
                    Glide.with(context)
                        .load(RetrofitService().getUrl() + "/user/getProfileImage/" + message.sender.email)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .circleCrop()
                        .into(holder.profileImageMessageLeft)
                else if (position + 1 == messages.size)
                    Glide.with(context)
                        .load(RetrofitService().getUrl() + "/user/getProfileImage/" + message.sender.email)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .circleCrop()
                        .into(holder.profileImageMessageLeft)
            }
        } else if (message.type == MessageType.LIKE) { // ********************     Like     *******************
            if (message.sender.email != chatMyChatsDto.userInfo.email) { // ********************     Like right     *******************
                holder.timeLikeRight.text =
                    "${fixTime(message.sendDate.hours - (message.sendDate.timezoneOffset / 60))}:${
                        fixTime(
                            message.sendDate.minutes
                        )
                    }"
                holder.animationLikeRight.playAnimation()
            } else { // ********************     Like left     *******************
                holder.timeLikeLeft.text =
                    "${fixTime(message.sendDate.hours - (message.sendDate.timezoneOffset / 60))}:${
                        fixTime(
                            message.sendDate.minutes
                        )
                    }"
                holder.animationLikeLeft.playAnimation()

                if (position + 1 < messages.size && message.sender.email != messages[position + 1].sender.email)
                    Glide.with(context)
                        .load(RetrofitService().getUrl() + "/user/getProfileImage/" + message.sender.email)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .circleCrop()
                        .into(holder.profileImageLikeLeft)
                else if (position + 1 == messages.size)
                    Glide.with(context)
                        .load(RetrofitService().getUrl() + "/user/getProfileImage/" + message.sender.email)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .circleCrop()
                        .into(holder.profileImageLikeLeft)
            }
        }
    }

    enum class MessagePositionType {
        FIRST,
        CENTER,
        LAST,
        ONLY
    }

    private fun fixTime(time: Int): String {
        return if (time < 10) "0$time"
        else time.toString()
    }

    override fun getItemViewType(position: Int): Int {
        this.position = position

        if (messages[position].type == MessageType.FIRST_MESSAGE)
            return R.layout.list_message_first_message
        else if (messages[position].type == MessageType.MESSAGE)
            if (messages[position].sender.email != chatMyChatsDto.userInfo.email)
                return R.layout.list_message_message_right
            else return R.layout.list_message_message_left
        else if (messages[position].type == MessageType.LIKE)
            if (messages[position].sender.email != chatMyChatsDto.userInfo.email)
                return R.layout.list_message_like_right
            else return R.layout.list_message_like_left

        return R.layout.list_request
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(items: List<MessageInfo>) {
        messages = items
        notifyDataSetChanged()
    }

    fun insert(items: List<MessageInfo>, count: Int) {
        messages = items
        for (i in 0 until count) {
            notifyItemChanged(messages.size - 2)
            notifyItemInserted(messages.size)
        }
    }
}