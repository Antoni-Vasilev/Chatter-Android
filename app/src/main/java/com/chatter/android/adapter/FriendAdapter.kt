package com.chatter.android.adapter

import android.annotation.SuppressLint
import android.content.Context
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
import com.chatter.android.database.Database
import com.chatter.android.model.FriendUserInfo
import com.chatter.android.model.UserLoginOutDto
import com.chatter.android.retrofit.RetrofitService
import java.util.*

class FriendAdapter(private val context: Context, private var list: List<FriendUserInfo>) :
    RecyclerView.Adapter<FriendAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView
        val dotIsOnline: LinearLayout
        val displayName: TextView
        val displayNameCode: TextView

        init {
            profileImage = itemView.findViewById(R.id.profileImage)
            dotIsOnline = itemView.findViewById(R.id.dotIsOnline)
            displayName = itemView.findViewById(R.id.displayName)
            displayNameCode = itemView.findViewById(R.id.displayNameCode)
        }
    }

    private lateinit var database: Database

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_friend, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friendUserInfo = list[position]
        database = Database(context)


        if (database.readObject<UserLoginOutDto>("myInfo").email.equals(friendUserInfo.secondUser)) {
            holder.displayName.text = friendUserInfo.firstUser.displayName
            holder.displayNameCode.text = friendUserInfo.firstUser.displayNameCode

            Glide.with(context)
                .load(
                    RetrofitService.getUrl() + "/user/getProfileImage" + database.readObject<UserLoginOutDto>(
                        "myInfo"
                    ).email
                )
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop()
                .into(holder.profileImage)

            if (friendUserInfo.firstUser.lastOpen != null) {
                if (Date().time - 1500 > friendUserInfo.firstUser.lastOpen.time) {
                    holder.dotIsOnline.isVisible = false
                }
            }
        } else {
            holder.displayName.text = friendUserInfo.secondUser.displayName
            holder.displayNameCode.text = friendUserInfo.secondUser.displayNameCode

            Glide.with(context)
                .load(
                    RetrofitService.getUrl() + "/user/getProfileImage/" + database.readObject<UserLoginOutDto>(
                        "myInfo"
                    ).email
                )
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop()
                .into(holder.profileImage)

            if (friendUserInfo.secondUser.lastOpen != null) {
                if (Date().time - 1500 > friendUserInfo.secondUser.lastOpen.time) {
                    holder.dotIsOnline.isVisible = false
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(items: List<FriendUserInfo>) {
        list = items
        notifyDataSetChanged()
    }
}