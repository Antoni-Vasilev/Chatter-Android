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
import com.chatter.android.adapter.RequestsAdapter
import com.chatter.android.database.Database
import com.chatter.android.model.friendRequest.FriendRequestAll
import com.chatter.android.model.user.UserLoginOutDto
import com.chatter.android.retrofit.FriendRequestController
import com.chatter.android.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchField: EditText
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var requestsAdapter: RequestsAdapter
    private lateinit var requests: List<FriendRequestAll>
    private lateinit var thiss: Context
    private lateinit var database: Database

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_request, container, false)
        thiss = view.context

        init(view)
        onLoad()

        return view
    }

    private fun init(view: View) {
        recyclerView = view.findViewById(R.id.recycler)
        searchField = view.findViewById(R.id.searchField)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)

        requests = listOf()
        requestsAdapter = RequestsAdapter(thiss, requests)
        database = Database(thiss)
    }

    private fun onLoad() {
        recyclerView.adapter = requestsAdapter
        recyclerView.layoutManager = LinearLayoutManager(thiss)
        recyclerView.setHasFixedSize(true)

        loadRequests("")

        searchField.addTextChangedListener { loadRequests(searchField.text.toString()) }

        swipeRefresh.setOnRefreshListener { loadRequests(searchField.text.toString()) }
    }

    private fun loadRequests(search: String) {
        val retrofit = RetrofitService()
        val friendRequestController: FriendRequestController =
            retrofit.retrofit.create(FriendRequestController::class.java)

        friendRequestController.all(search, database.readObject<UserLoginOutDto>("myInfo").email)
            .enqueue(object : Callback<List<FriendRequestAll>> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<List<FriendRequestAll>>,
                    response: Response<List<FriendRequestAll>>
                ) {
                    requests = response.body()!!
                    requestsAdapter.update(requests)
                }

                override fun onFailure(call: Call<List<FriendRequestAll>>, t: Throwable) {
                }
            })

        swipeRefresh.isRefreshing = false
    }
}