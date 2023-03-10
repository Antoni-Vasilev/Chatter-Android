package com.chatter.android.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chatter.android.R
import com.chatter.android.database.Database
import com.chatter.android.fragment.FriendBottomListItemFragment
import com.chatter.android.model.friend.FriendUserInfo
import com.chatter.android.model.user.UserLoginOutDto
import com.chatter.android.retrofit.RetrofitService
import java.util.*

@Suppress("SENSELESS_COMPARISON", "DEPRECATION")
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


        if (database.readObject<UserLoginOutDto>("myInfo").email == friendUserInfo.secondUser.email) {
            holder.displayName.text = friendUserInfo.firstUser.displayName
            holder.displayNameCode.text = friendUserInfo.firstUser.displayNameCode

            Glide.with(context)
                .load(
                    RetrofitService().getUrl() + "/user/getProfileImage/" + friendUserInfo.firstUser.email
                )
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop()
                .into(holder.profileImage)

            if (friendUserInfo.firstUser.lastOpen != null) {
                println(friendUserInfo.firstUser.lastOpen.timezoneOffset)
                holder.dotIsOnline.isVisible =
                    !Date(friendUserInfo.firstUser.lastOpen.time - friendUserInfo.firstUser.lastOpen.timezoneOffset * 60 * 1000).before(
                        Date(Date().time - 30000)
                    )
            } else holder.dotIsOnline.isVisible = false
        } else {
            holder.displayName.text = friendUserInfo.secondUser.displayName
            holder.displayNameCode.text = friendUserInfo.secondUser.displayNameCode

            Glide.with(context)
                .load(
                    RetrofitService().getUrl() + "/user/getProfileImage/" + friendUserInfo.secondUser.email
                )
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop()
                .into(holder.profileImage)

            if (friendUserInfo.secondUser.lastOpen != null) {
                println(Date(friendUserInfo.secondUser.lastOpen.time - friendUserInfo.secondUser.lastOpen.timezoneOffset * 60 * 1000))
                holder.dotIsOnline.isVisible =
                    !Date(friendUserInfo.secondUser.lastOpen.time - friendUserInfo.secondUser.lastOpen.timezoneOffset * 60 * 1000).before(
                        Date(Date().time - 30000)
                    )
            } else holder.dotIsOnline.isVisible = false
        }

        holder.itemView.setOnClickListener {
            val email =
                if (database.readObject<UserLoginOutDto>("myInfo").email == friendUserInfo.secondUser.email) friendUserInfo.firstUser.email
                else friendUserInfo.secondUser.email
            FriendBottomListItemFragment(email).show(
                (context as AppCompatActivity).supportFragmentManager,
                "userInfo"
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(items: List<FriendUserInfo>) {
        list = items
        notifyDataSetChanged()
    }
}