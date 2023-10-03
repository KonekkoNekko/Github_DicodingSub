package com.dicoding.githubuserapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.githubuserapp.data.local.entity.FavoriteUser

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM favorite")
    fun getAllFavorites(): LiveData<List<FavoriteUser>>

    @Query("SELECT count(*) FROM favorite WHERE id = :id")
    suspend fun checkFav(id: Int): Int

    @Query("DELETE FROM favorite WHERE id = :id")
    suspend fun removeFromFavorites(id: Int): Int
}

