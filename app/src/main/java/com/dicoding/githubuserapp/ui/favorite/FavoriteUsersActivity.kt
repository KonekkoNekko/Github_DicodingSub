package com.dicoding.githubuserapp.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.data.local.entity.FavoriteUser
import com.dicoding.githubuserapp.data.remote.response.ItemsItem
import com.dicoding.githubuserapp.databinding.ActivityFavoriteUsersBinding
import com.dicoding.githubuserapp.ui.UserAdapter
import com.dicoding.githubuserapp.ui.detail.DetailActivity

class FavoriteUsersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUsersBinding
    private val favoriteUsersViewModel by viewModels<FavoriteUsersViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "User Favorit Kamu!"
        showLoading(true)
        val layoutManager = LinearLayoutManager(this)
        binding.rvUserFav.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUserFav.addItemDecoration(itemDecoration)

        val adapter = UserAdapter(object : UserAdapter.OnItemClickListener {
            override fun onItemClick(user: ItemsItem) {
                Intent(this@FavoriteUsersActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
                    it.putExtra(DetailActivity.EXTRA_ID, user.id)
                    startActivity(it)
                }
            }
        })

        binding.rvUserFav.adapter = adapter

        favoriteUsersViewModel.getFavorites()?.observe(this, Observer<List<FavoriteUser>> { favoriteUsers ->
            val mappedList = favoriteUsers.map { favoriteUser ->
                ItemsItem(
                    id = favoriteUser.id,
                    login = favoriteUser.login,
                    avatarUrl = favoriteUser.avatarUrl
                    // Map other properties accordingly
                )
            }
            // Update the adapter with the new list of ItemsItem
            adapter.submitList(mappedList)
        })
        showLoading(false)
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
