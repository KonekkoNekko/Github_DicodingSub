// DetailActivity.kt
package com.dicoding.githubuserapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.data.remote.response.DetailResponse
import com.dicoding.githubuserapp.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val username = intent.getStringExtra(EXTRA_USERNAME)

        detailViewModel.setUserDetail(username.toString())
        detailViewModel.detail.observe(this) { detailResponse ->
            binding.apply {
                Glide.with(binding.root).load(detailResponse.avatarUrl)
                    .transition(DrawableTransitionOptions.withCrossFade()).centerCrop().circleCrop()
                    .into(binding.ivUserDetail)
                tvUserName.text = detailResponse.name.toString()
                tvUserLogin.text = detailResponse.login.toString()
                tvNumberFollowers.text = detailResponse.followers.toString()
                tvNumberFollowing.text = detailResponse.following.toString()
            }
        }

        detailViewModel.isFavorite.observe(this) { isFavorite ->
            showFavToggle(isFavorite)

            binding.toggleFavorite.setOnClickListener {
                toggleFavorite(detailViewModel.detail.value)
            }
        }

        val sectionPagerAdapter = SectionsPagerAdapter(this)
        sectionPagerAdapter.setUsername(username.toString())
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getText(TAB_TITLES[position])
        }.attach()
    }

    private fun showFavToggle(isFavorite: Boolean) {
        binding.toggleFavorite.isChecked = isFavorite
    }

    private fun toggleFavorite(detailResponse: DetailResponse?) {
        if (detailResponse != null) {
            val username = detailResponse.login.toString()
            val id = detailResponse.id.toString().toInt()
            val avatarUrl = detailResponse.avatarUrl.toString()
            if (binding.toggleFavorite.isChecked) {
                detailViewModel.addToFavorite(username, id, avatarUrl)
            } else {
                detailViewModel.removeFromFavorite(id)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}
