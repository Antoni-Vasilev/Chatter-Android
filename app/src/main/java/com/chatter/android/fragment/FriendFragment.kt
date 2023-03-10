package com.chatter.android.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chatter.android.R
import com.chatter.android.adapter.FriendAdapter
import com.chatter.android.database.Database
import com.chatter.android.model.friend.FriendUserInfo
import com.chatter.android.model.user.UserLoginOutDto
import com.chatter.android.retrofit.FriendController
import com.chatter.android.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var searchField: EditText
    private lateinit var swipeRefresh: SwipeRefreshLayout

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
        searchField = view.findViewById(R.id.searchField)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)

        friendAdapter = FriendAdapter(thiss, friends)
    }

    private fun onLoad() {
        recycler.layoutManager = LinearLayoutManager(thiss)
        recycler.setHasFixedSize(true)
        recycler.adapter = friendAdapter

        getInfo("")
        searchField.addTextChangedListener { getInfo(searchField.text.toString()) }

        swipeRefresh.setOnRefreshListener { getInfo(searchField.text.toString()) }
    }

    private fun getInfo(search: String) {
        val retrofit = RetrofitService()
        val friendController: FriendController =
            retrofit.retrofit.create(FriendController::class.java)

        friendController.getAllByEmail(
            database.readObject<UserLoginOutDto>("myInfo").email,
            search
        )
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

        swipeRefresh.isRefreshing = false
    }
}