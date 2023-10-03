package com.dicoding.githubuserapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite")
data class FavoriteUser(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "login") val login: String,
    @ColumnInfo(name = "avatarUrl") val avatarUrl: String
) : Serializable