package com.dicoding.githubuserapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.data.remote.response.ItemsItem
import com.dicoding.githubuserapp.databinding.ActivityMainBinding
import com.dicoding.githubuserapp.switchmode.SettingPreferences
import com.dicoding.githubuserapp.switchmode.SwitchModeViewModel
import com.dicoding.githubuserapp.switchmode.ViewModelFactory
import com.dicoding.githubuserapp.switchmode.dataStore
import com.dicoding.githubuserapp.ui.detail.DetailActivity
import com.dicoding.githubuserapp.ui.favorite.FavoriteUsersActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private var isSwitchModeChecked = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.btnSearch.setOnClickListener {
            searchUser()
        }

        binding.etQuery.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                searchUser()
                return@setOnKeyListener true
            }
            false
        }

        val adapter = UserAdapter(object : UserAdapter.OnItemClickListener {
            override fun onItemClick(user: ItemsItem) {
                Intent(this@MainActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
                    it.putExtra(DetailActivity.EXTRA_ID, user.id)
                    startActivity(it)
                }
            }
        })

        mainViewModel.listUser.observe(this) { listUsers ->
            adapter.submitList(listUsers)
        }

        binding.rvUser.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite_menu -> {
                Intent(this, FavoriteUsersActivity::class.java).also {
                    startActivity(it)
                }
            }
            R.id.switch_mode -> {
                isSwitchModeChecked = !isSwitchModeChecked
                item.isChecked = isSwitchModeChecked
                val pref = SettingPreferences.getInstance(application.dataStore)
                val switchModeViewModel = ViewModelProvider(
                    this,
                    ViewModelFactory(pref)
                )[SwitchModeViewModel::class.java]
                switchModeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
                    isSwitchModeChecked = if (isDarkModeActive) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        true
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        false
                    }
                }
                switchModeViewModel.saveThemeSetting(item.isChecked)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun searchUser() {
        binding.apply {
            val query = etQuery.text.toString()
            if (query.isEmpty()) return
            mainViewModel.setSearchUsers(query)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
