package com.dicoding.githubuserapp.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserapp.data.response.FollowersResponseItem
import com.dicoding.githubuserapp.data.response.FollowingResponseItem
import com.dicoding.githubuserapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerFollowingViewModel : ViewModel() {
    private val _followingListUser = MutableLiveData<List<FollowingResponseItem?>>()
    val followingListUser: LiveData<List<FollowingResponseItem?>> = _followingListUser

    private val _followerListUser = MutableLiveData<List<FollowersResponseItem?>>()
    val followerListUser: LiveData<List<FollowersResponseItem?>> = _followerListUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "FollowerFollowingViewModel"
    }

    fun getFollowing(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(query)
        client.enqueue(object : Callback<List<FollowingResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowingResponseItem>>,
                response: Response<List<FollowingResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.d("apakah masuk response body following", response.body().toString())
                    _followingListUser.value = response.body() ?: emptyList()
                } else {
                    Log.e(FollowerFollowingViewModel.TAG, "Gagal: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FollowingResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(FollowerFollowingViewModel.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getFollower(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(query)
        client.enqueue(object : Callback<List<FollowersResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowersResponseItem>>,
                response: Response<List<FollowersResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followerListUser.value = response.body() ?: emptyList()
                } else {
                    Log.e(FollowerFollowingViewModel.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FollowersResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(FollowerFollowingViewModel.TAG, "onFailure: ${t.message}")
            }
        })
    }
}