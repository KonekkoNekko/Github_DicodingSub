package com.dicoding.githubuserapp.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.data.response.FollowersResponseItem
import com.dicoding.githubuserapp.data.response.FollowingResponseItem
import com.dicoding.githubuserapp.databinding.FragmentFollowerFollowingBinding

class FollowerFollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowerFollowingBinding
    private val followerFollowingViewModel by viewModels<FollowerFollowingViewModel>()
    private var username: String? = null
    private var isFollower: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowerFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followerFollowingViewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollow.addItemDecoration(itemDecoration)

        val adapterA = FollowerAdapter(object : FollowerAdapter.OnItemClickListener {
            override fun onItemClick(user: FollowersResponseItem) {
                TODO("Not yet implemented")
            }
        })

        val adapterB = FollowingAdapter(object : FollowingAdapter.OnItemClickListener {
            override fun onItemClick(user: FollowingResponseItem) {
                TODO("Not yet implemented")
            }
        })

// Before the if statement
        binding.rvFollow.adapter = adapterA // Set the adapterA by default

        if (arguments != null) {
            isFollower = requireArguments().getBoolean("Follower")
            username = requireArguments().getString("Username")
            if (isFollower!!) {
                followerFollowingViewModel.getFollower(username!!)
                followerFollowingViewModel.followerListUser.observe(viewLifecycleOwner) { listFollower ->
                    Log.d("cek isi list follower", listFollower.toString())
                    adapterA.submitList(listFollower)
                }
                Log.d("masuk IF", isFollower.toString())
                Log.d("masuk IF", username.toString())
            } else {
                followerFollowingViewModel.getFollowing(username!!)
                followerFollowingViewModel.followingListUser.observe(viewLifecycleOwner) { listFollowing ->
                    Log.d("cek isi list following", listFollowing.toString())
                    adapterB.submitList(listFollowing)
                }
                binding.rvFollow.adapter = adapterB // Set the adapterB for following scenario
                Log.d("masuk ELSE", isFollower.toString())
                Log.d("masuk ELSE", username.toString())
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}