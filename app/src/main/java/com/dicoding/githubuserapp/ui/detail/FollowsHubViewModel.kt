package com.dicoding.githubuserapp.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserapp.data.response.FollowsResponseItem
import com.dicoding.githubuserapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowsHubViewModel : ViewModel() {
    private val _followingListUser = MutableLiveData<List<FollowsResponseItem?>>()
    val followingListUser: LiveData<List<FollowsResponseItem?>> = _followingListUser

    private val _followerListUser = MutableLiveData<List<FollowsResponseItem?>>()
    val followerListUser: LiveData<List<FollowsResponseItem?>> = _followerListUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "FollowsHubViewModel"
    }

    fun getFollowing(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(query)
        client.enqueue(object : Callback<List<FollowsResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowsResponseItem>>,
                response: Response<List<FollowsResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followingListUser.value = response.body() ?: emptyList()
                } else {
                    Log.e(FollowsHubViewModel.TAG, "Gagal: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FollowsResponseItem>>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun getFollower(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(query)
        client.enqueue(object : Callback<List<FollowsResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowsResponseItem>>,
                response: Response<List<FollowsResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followerListUser.value = response.body() ?: emptyList()
                } else {
                    Log.e(FollowsHubViewModel.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FollowsResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(FollowsHubViewModel.TAG, "onFailure: ${t.message}")
            }
        })
    }
}