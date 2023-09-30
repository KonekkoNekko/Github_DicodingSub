package com.dicoding.githubuserapp.ui.detail

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dicoding.githubuserapp.data.response.FollowersResponseItem
import com.dicoding.githubuserapp.databinding.ItemUserBinding


class FollowerAdapter(private val itemClickListener: OnItemClickListener) :
    ListAdapter<FollowersResponseItem, FollowerAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MyViewHolder(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val user = getItem(position)
                    itemClickListener.onItemClick(user)
                }
            }
        }

        fun bind(user: FollowersResponseItem) {
            Log.d("cekisiadapterahay", user.toString())
            Glide.with(binding.root).load(user.avatarUrl)
                .transition(DrawableTransitionOptions.withCrossFade()).centerCrop().circleCrop()
                .into(binding.ivUser)
            binding.tvName.text = user.login
            binding.tvUser.text = user.type
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FollowersResponseItem>() {
            override fun areItemsTheSame(
                oldItem: FollowersResponseItem,
                newItem: FollowersResponseItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: FollowersResponseItem,
                newItem: FollowersResponseItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(user: FollowersResponseItem)
    }
}