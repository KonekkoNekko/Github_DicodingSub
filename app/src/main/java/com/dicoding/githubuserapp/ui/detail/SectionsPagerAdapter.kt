package com.dicoding.githubuserapp.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private lateinit var username: String
    override fun getItemCount(): Int {
        return 2
    }

    fun setUsername(username: String) {
        this.username = username
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> {
                fragment = FollowsHubFragment()
                val bundle = Bundle()
                bundle.putString("Username", username)
                bundle.putBoolean("Follower", true)
                fragment.arguments = bundle

            }
            1 -> {
                fragment = FollowsHubFragment()
                val bundle = Bundle()
                bundle.putString("Username", username)
                bundle.putBoolean("Follower", false)
                fragment.arguments = bundle
            }

        }
        return fragment as Fragment
    }
}