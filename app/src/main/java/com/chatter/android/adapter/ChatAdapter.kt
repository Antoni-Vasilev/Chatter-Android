package com.chatter.android.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chatter.android.R
import com.chatter.android.activity.ChatActivity
import com.chatter.android.model.chat.ChatMyChatsDto
import com.chatter.android.retrofit.RetrofitService
import java.text.SimpleDateFormat
import java.util.*

@Suppress("SENSELESS_COMPARISON", "DEPRECATION")
class ChatAdapter(
    private val context: Context,
    private var chats: List<ChatMyChatsDto>
) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var profileImage: ImageView = itemView.findViewById(R.id.profileImage)
        var dotIsOnline: LinearLayout = itemView.findViewById(R.id.dotIsOnline)
        var fullName: TextView = itemView.findViewById(R.id.fullName)
        var lastMessage: TextView = itemView.findViewById(R.id.lastMessage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.list_chat, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat: ChatMyChatsDto = chats[position]

        holder.fullName.text = chat.userInfo.fullName

        if (chat.lastMessage != null) {
            val date = chat.lastMessage.sendDate
            val output: String
            if (Date().year != date.year) output = "${
                SimpleDateFormat("MMM").format(
                    SimpleDateFormat("dd-MM-yyyy").parse("${date.date}-${date.month + 1}-${date.year + 1900}") as Date
                )
            } ${date.date}, ${date.year + 1900}"
            else if (Date().month != date.month) output = "${
                SimpleDateFormat("MMM").format(
                    SimpleDateFormat("dd-MM-yyyy").parse("${date.date}-${date.month + 1}-${date.year + 1900}") as Date
                )
            } ${date.date}"
            else if (Date().date == date.date) output =
                "${fixTime(date.hours - (date.timezoneOffset / 60))}:${fixTime(date.minutes)}"
            else if (Date().date - 7 < date.date && Date().date + 7 > date.date) output =
                SimpleDateFormat("EE").format(
                    SimpleDateFormat("dd-MM-yyyy").parse("${date.date}-${date.month + 1}-${date.year + 1900}") as Date
                )
            else output =
                "${
                    SimpleDateFormat("MMM").format(
                        SimpleDateFormat("dd-MM-yyyy").parse("${date.date}-${date.month + 1}-${date.year + 1900}") as Date
                    )
                } ${date.date}, ${
                    SimpleDateFormat("EE").format(
                        SimpleDateFormat("dd-MM-yyyy").parse("${date.date}-${date.month + 1}-${date.year + 1900}") as Date
                    )
                }"

            println("${date.date}-${date.month + 1}-${date.year + 1900}")

            holder.lastMessage.text = "${chat.lastMessage.message} • $output"
            if (chat.lastMessage.sendDate.time < System.currentTimeMillis() - 15000) holder.dotIsOnline.isVisible =
                false
        } else {
            holder.lastMessage.text = context.getString(R.string.no_message_available)
        }

        if (chat.userInfo.lastOpen != null) {
            println(Date(chat.userInfo.lastOpen.time - chat.userInfo.lastOpen.timezoneOffset * 60 * 1000))
            holder.dotIsOnline.isVisible =
                !Date(chat.userInfo.lastOpen.time - chat.userInfo.lastOpen.timezoneOffset * 60 * 1000).before(
                    Date(Date().time - 30000)
                )
        } else holder.dotIsOnline.isVisible = false

        Glide.with(context)
            .load(RetrofitService().getUrl() + "/user/getProfileImage/" + chat.userInfo.email)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .into(holder.profileImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("chatId", chat.id)
            context.startActivity(intent)
        }
    }

    private fun fixTime(time: Int): String {
        return if (time < 10) "0$time"
        else time.toString()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(items: List<ChatMyChatsDto>) {
        chats = items
        notifyDataSetChanged()
    }
}