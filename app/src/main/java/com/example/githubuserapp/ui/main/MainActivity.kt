package com.example.githubuserapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.R
import com.example.githubuserapp.data.response.ItemsItem
import com.example.githubuserapp.databinding.ActivityMainBinding
import com.example.githubuserapp.ui.ListUserAdapter
import com.example.githubuserapp.ui.ViewModelFactory
import com.example.githubuserapp.ui.favorite.FavoriteActivity
import com.example.githubuserapp.ui.settings.SettingsActivity
import com.example.githubuserapp.ui.settings.SettingsViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val settingsViewModel by viewModels<SettingsViewModel> {
            ViewModelFactory.getInstance(application)
        }

        settingsViewModel.getThemeSettings().observe(this) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        setContentView(binding?.root)

        mainViewModel.listUser.observe(this) { user ->
            setListUserData(user)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.isNoData.observe(this){
            it.getContentIfNotHandled()?.let {
                Snackbar.make(
                    window.decorView.rootView,
                    "User Not Found!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding?.rvListUser?.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding?.rvListUser?.addItemDecoration(itemDecoration)
        
        with(binding) {
            this!!.searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.text = searchView.text
                    searchView.hide()
                    mainViewModel.findUser(searchView.text.toString())
                    false
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.action_favorite -> {
                val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setListUserData(user: List<ItemsItem>?) {
        val adapter = ListUserAdapter()
        adapter.submitList(user)
        binding?.rvListUser?.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.isVisible = isLoading
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}