package com.dicoding.githubuserapp.data.retrofit

import com.dicoding.githubuserapp.data.response.DetailResponse
import com.dicoding.githubuserapp.data.response.FollowersResponseItem
import com.dicoding.githubuserapp.data.response.FollowingResponseItem
import com.dicoding.githubuserapp.data.response.SearchResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUsers(
        @Query("q") query: String
    ) : Call<SearchResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ) : Call<DetailResponse>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("username") username: String
    ) : Call<List<FollowingResponseItem>>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path("username") username: String
    ) : Call<List<FollowersResponseItem>>
}