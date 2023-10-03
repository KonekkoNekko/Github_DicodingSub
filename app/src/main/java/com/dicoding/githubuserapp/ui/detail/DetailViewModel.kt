// DetailViewModel.kt
package com.dicoding.githubuserapp.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.githubuserapp.data.local.entity.FavoriteUser
import com.dicoding.githubuserapp.data.local.room.FavoriteUserDao
import com.dicoding.githubuserapp.data.local.room.FavoriteUserDatabase
import com.dicoding.githubuserapp.data.remote.response.DetailResponse
import com.dicoding.githubuserapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _detail = MutableLiveData<DetailResponse>()
    val detail: LiveData<DetailResponse> = _detail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    companion object {
        private const val TAG = "DetailViewModel"
    }

    private var userDao: FavoriteUserDao?
    private var userDb: FavoriteUserDatabase?

    init {
        userDb = FavoriteUserDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    fun setUserDetail(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetail(query)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detail.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }

                CoroutineScope(Dispatchers.IO).launch {
                    val id = response.body()?.id ?: 0
                    val checky = checkUser(id)
                    val isFavorite = checky == 1
                    withContext(Dispatchers.Main) {
                        _isFavorite.value = isFavorite
                    }
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun addToFavorite(username: String, id: Int, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(
                id = id,
                login = username,
                avatarUrl = avatarUrl
            )
            userDao?.addToFavorites(user)
        }
    }

    suspend fun checkUser(id: Int) = userDao?.checkFav(id)

    fun removeFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromFavorites(id)
        }
    }
}
