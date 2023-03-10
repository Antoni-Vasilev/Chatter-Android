package com.chatter.android.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chatter.android.R
import com.chatter.android.adapter.NewChatAdapter
import com.chatter.android.database.Database
import com.chatter.android.model.user.UserInfoDto
import com.chatter.android.model.user.UserLoginOutDto
import com.chatter.android.retrofit.RetrofitService
import com.chatter.android.retrofit.UserController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class NewChatFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var searchField: EditText
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var database: Database
    private var users: List<UserInfoDto> = listOf()
    private lateinit var newChatAdapter: NewChatAdapter
    private lateinit var thiss: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_chat, container, false)
        thiss = view.context

        init(view)
        onLoad()

        return view
    }

    private fun init(view: View) {
        recycler = view.findViewById(R.id.recycler)
        searchField = view.findViewById(R.id.searchField)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)

        database = Database(thiss)
    }

    private fun onLoad() {
        newChatAdapter = NewChatAdapter(users, thiss)
        recycler.adapter = newChatAdapter
        recycler.layoutManager = LinearLayoutManager(thiss)
        recycler.setHasFixedSize(true)

        val retrofit: Retrofit = RetrofitService().retrofit
        val userController: UserController = retrofit.create(UserController::class.java)

        loadUsers()

        searchField.addTextChangedListener {
            userController.allUser(
                searchField.text.toString().trim(),
                database.readObject<UserLoginOutDto>("myInfo").email
            )
                .enqueue(object : Callback<List<UserInfoDto>> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<List<UserInfoDto>>,
                        response: Response<List<UserInfoDto>>
                    ) {
                        newChatAdapter.updateList(response.body()!!)
                    }

                    override fun onFailure(call: Call<List<UserInfoDto>>, t: Throwable) {
                        Toast.makeText(
                            thiss,
                            getString(R.string.the_connection_to_the_server_was_lost),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }

        swipeRefresh.setOnRefreshListener {
            loadUsers()
        }
    }

    private fun loadUsers() {
        val retrofit: Retrofit = RetrofitService().retrofit
        val userController: UserController = retrofit.create(UserController::class.java)

        userController.allUser("", database.readObject<UserLoginOutDto>("myInfo").email)
            .enqueue(object : Callback<List<UserInfoDto>> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<List<UserInfoDto>>,
                    response: Response<List<UserInfoDto>>
                ) {
                    newChatAdapter.updateList(response.body()!!)
                }

                override fun onFailure(call: Call<List<UserInfoDto>>, t: Throwable) {
                    Toast.makeText(
                        thiss,
                        getString(R.string.the_connection_to_the_server_was_lost),
                        Toast.LENGTH_LONG
                    ).show()
                }

            })

        swipeRefresh.isRefreshing = false
    }
}