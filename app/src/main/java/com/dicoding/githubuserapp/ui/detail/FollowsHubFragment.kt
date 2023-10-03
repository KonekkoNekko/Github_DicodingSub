package com.dicoding.githubuserapp.ui.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.data.remote.response.FollowsResponseItem
import com.dicoding.githubuserapp.databinding.FragmentFollowerFollowingBinding

class FollowsHubFragment : Fragment() {
    private lateinit var binding: FragmentFollowerFollowingBinding
    private val followsHubViewModel by viewModels<FollowsHubViewModel>()
    private var username: String? = null
    private var isFollower: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowerFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followsHubViewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollow.addItemDecoration(itemDecoration)

        val adapter = FollowsHubAdapter(object : FollowsHubAdapter.OnItemClickListener {
            override fun onItemClick(user: FollowsResponseItem) {
                Intent(activity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
                    startActivity(it)
                }
            }
        })

// Before the if statement
        binding.rvFollow.adapter = adapter // Set the adapterA by default

        if (arguments != null) {
            isFollower = requireArguments().getBoolean("Follower")
            username = requireArguments().getString("Username")
            if (isFollower!!) {
                followsHubViewModel.getFollower(username!!)
                followsHubViewModel.followerListUser.observe(viewLifecycleOwner) { listFollower ->
                    Log.d("cek isi list follower", listFollower.toString())
                    adapter.submitList(listFollower)
                }
                Log.d("masuk IF", isFollower.toString())
                Log.d("masuk IF", username.toString())
            } else {
                followsHubViewModel.getFollowing(username!!)
                followsHubViewModel.followingListUser.observe(viewLifecycleOwner) { listFollowing ->
                    Log.d("cek isi list following", listFollowing.toString())
                    adapter.submitList(listFollowing)
                }
                binding.rvFollow.adapter = adapter // Set the adapterB for following scenario
                Log.d("masuk ELSE", isFollower.toString())
                Log.d("masuk ELSE", username.toString())
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}