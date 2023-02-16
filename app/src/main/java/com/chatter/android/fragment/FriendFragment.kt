package com.chatter.android.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chatter.android.R
import com.chatter.android.adapter.FriendAdapter
import com.chatter.android.database.Database
import com.chatter.android.model.FriendUserInfo
import com.chatter.android.model.UserLoginOutDto
import com.chatter.android.retrofit.FriendController
import com.chatter.android.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendFragment : Fragment() {

    private lateinit var recycler: RecyclerView

    private lateinit var friendAdapter: FriendAdapter
    private lateinit var thiss: Context
    private lateinit var database: Database
    private var friends: List<FriendUserInfo> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_friend, container, false)
        thiss = view.context
        database = Database(thiss)

        init(view)
        onLoad()

        return view
    }

    private fun init(view: View) {
        recycler = view.findViewById(R.id.recycler)

        friendAdapter = FriendAdapter(thiss, friends)
    }

    private fun onLoad() {
        recycler.layoutManager = LinearLayoutManager(thiss)
        recycler.setHasFixedSize(true)
        recycler.adapter = friendAdapter

        val retrofit = RetrofitService()
        val friendController: FriendController = retrofit.retrofit.create(FriendController::class.java)

        friendController.getAllByEmail(database.readObject<UserLoginOutDto>("myInfo").email)
            .enqueue(object : Callback<List<FriendUserInfo>> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<List<FriendUserInfo>>,
                    response: Response<List<FriendUserInfo>>
                ) {
                    friends = response.body()!!
                    friendAdapter.update(friends)
                }

                override fun onFailure(call: Call<List<FriendUserInfo>>, t: Throwable) {

                }

            })
    }
}