package com.dicoding.githubuserapp.data.remote.retrofit

import com.dicoding.githubuserapp.data.remote.response.DetailResponse
import com.dicoding.githubuserapp.data.remote.response.FollowsResponseItem
import com.dicoding.githubuserapp.data.remote.response.SearchResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getSearchUsers(
        @Query("q") query: String
    ) : Call<SearchResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ) : Call<DetailResponse>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ) : Call<List<FollowsResponseItem>>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ) : Call<List<FollowsResponseItem>>
}