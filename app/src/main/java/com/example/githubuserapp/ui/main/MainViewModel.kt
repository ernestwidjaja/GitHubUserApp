package com.example.githubuserapp.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapp.data.response.GithubResponse
import com.example.githubuserapp.data.response.ItemsItem
import com.example.githubuserapp.data.retrofit.ApiConfig
import com.example.githubuserapp.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel(){
    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser: LiveData<List<ItemsItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isNoData = MutableLiveData<Event<Boolean>>()
    val isNoData: LiveData<Event<Boolean>> = _isNoData

    private val username = "ernest"

    init {
        findUser(username)
    }

    fun findUser(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListUser(username)
        client.enqueue(object: Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if (response.body()?.items.isNullOrEmpty()) {
                        _isNoData.value = Event(true)
                    } else {
                        _listUser.value = response.body()?.items
                    }
                } else {
                    Log.e("error", response.message())
                    _isNoData.value = Event(true)
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("error", t.message.toString())
            }
        })
    }
}