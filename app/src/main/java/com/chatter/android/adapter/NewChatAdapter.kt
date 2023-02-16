package com.chatter.android.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chatter.android.R
import com.chatter.android.fragment.UserBottomInfoFragment
import com.chatter.android.model.UserInfoDto
import com.chatter.android.retrofit.RetrofitService

class NewChatAdapter(private var users: List<UserInfoDto>, private val context: Context) :
    RecyclerView.Adapter<NewChatAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var profileImage: ImageView
        var username: TextView
        var userCode: TextView

        init {
            profileImage = itemView.findViewById(R.id.profileImage)
            username = itemView.findViewById(R.id.username)
            userCode = itemView.findViewById(R.id.userCode)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_new_chat, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userInfoDto: UserInfoDto = users[position]

        holder.username.text = userInfoDto.displayName
        holder.userCode.text = userInfoDto.displayNameCode

        holder.itemView.setOnClickListener {
            UserBottomInfoFragment(userInfoDto).show(
                (context as AppCompatActivity).supportFragmentManager,
                "userInfo"
            )
        }

        Glide.with(context)
            .load(RetrofitService.getUrl() + "/user/getProfileImage/" + userInfoDto.email)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .into(holder.profileImage)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(items: List<UserInfoDto>) {
        removeItems(items)

        var i = 0
        while (i < items.size) {
            if (users.size != 0) {
                try {
                    if (users[i].id != items[i].id) {
                        val list: MutableList<UserInfoDto> = users.toMutableList()
                        list.add(i, items[i])
                        users = list.toList()
                        notifyItemInserted(i)
                    }
                } catch (e: Exception) {
                    users = items
                    notifyDataSetChanged()
                }
            } else {
                val list: MutableList<UserInfoDto> = users.toMutableList()
                list.add(i, items[i])
                users = list.toList()
                notifyItemInserted(i)
            }

            i++
        }
    }

    private fun removeItems(items: List<UserInfoDto>) {
        val removeList: MutableList<UserInfoDto> = mutableListOf()
        for (i in users.indices) {
            var isFound = false

            for (j in items.indices) {
                if (items[j].id == users[i].id) {
                    isFound = true
                    break
                }
            }

            if (!isFound) {
                removeList.add(users[i])
            }
        }

        removeList.forEach {
            notifyItemRemoved(users.indexOf(it))
            val list: MutableList<UserInfoDto> = users.toMutableList()
            list.removeAt(users.indexOf(it))
            users = list.toList()
        }
    }
}