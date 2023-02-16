package com.chatter.android.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chatter.android.R
import com.chatter.android.model.FriendRequestAll
import com.chatter.android.model.FriendRequestRegisterInDto
import com.chatter.android.retrofit.FriendRequestController
import com.chatter.android.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestsAdapter(
    private val context: Context,
    private var requests: List<FriendRequestAll>
) : RecyclerView.Adapter<RequestsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val profileImage: ImageView
        val fullName: TextView
        val displayName: TextView
        val acceptButton: Button
        val rejectButton: Button

        init {
            profileImage = itemView.findViewById(R.id.profileImage)
            fullName = itemView.findViewById(R.id.fullName)
            displayName = itemView.findViewById(R.id.displayName)
            acceptButton = itemView.findViewById(R.id.acceptButton)
            rejectButton = itemView.findViewById(R.id.rejectButton)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_request, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return requests.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: FriendRequestAll = requests[position]

        holder.fullName.text = item.from.fullName
        holder.displayName.text = item.from.displayName + " " + item.from.displayNameCode

        Glide.with(context)
            .load(RetrofitService.getUrl() + "/user/getProfileImage/" + item.from.email)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .into(holder.profileImage)

        holder.acceptButton.setOnClickListener {
            val retrofit = RetrofitService()
            val friendRequestController: FriendRequestController =
                retrofit.retrofit.create(FriendRequestController::class.java)

            val friendRequestRegisterInDto =
                FriendRequestRegisterInDto(item.from.email!!, item.to.email!!)

            friendRequestController.acceptRequest(friendRequestRegisterInDto)
                .enqueue(object : Callback<Boolean> {
                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                        if (response.code() == 200) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.the_request_was_accepted),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    }

                })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(items: List<FriendRequestAll>) {
        requests = items
        notifyDataSetChanged()
    }
}