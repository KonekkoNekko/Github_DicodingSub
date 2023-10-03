package com.dicoding.githubuserapp.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.githubuserapp.data.local.entity.FavoriteUser
import com.dicoding.githubuserapp.data.local.room.FavoriteUserDao
import com.dicoding.githubuserapp.data.local.room.FavoriteUserDatabase
import com.dicoding.githubuserapp.data.remote.response.ItemsItem

class FavoriteUsersViewModel(application: Application) : AndroidViewModel(application) {
    private var userDao: FavoriteUserDao?
    private var userDb: FavoriteUserDatabase?

    init {
        userDb = FavoriteUserDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    fun getFavorites(): LiveData<List<FavoriteUser>>? {
        return userDao?.getAllFavorites()
    }

    private fun FavoriteUser.toItemsItem(): ItemsItem {
        return ItemsItem(
            id = this.id,
            login = this.login,
            avatarUrl = this.avatarUrl
        )
    }
}
