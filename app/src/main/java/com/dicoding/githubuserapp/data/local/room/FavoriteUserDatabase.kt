package com.dicoding.githubuserapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.githubuserapp.data.local.entity.FavoriteUser

@Database(entities = [FavoriteUser::class], version = 1, exportSchema = false)
abstract class FavoriteUserDatabase : RoomDatabase() {
    companion object{
        var INSTANCE : FavoriteUserDatabase? = null

        fun getDatabase(context: Context): FavoriteUserDatabase?{
            if (INSTANCE == null){
                synchronized(FavoriteUserDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, FavoriteUserDatabase::class.java, "user_database").build()
                }
            }
            return INSTANCE
        }
    }
    abstract fun favoriteUserDao(): FavoriteUserDao
}