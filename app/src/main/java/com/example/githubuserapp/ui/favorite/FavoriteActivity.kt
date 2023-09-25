package com.example.githubuserapp.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.R
import com.example.githubuserapp.data.response.ItemsItem
import com.example.githubuserapp.databinding.ActivityFavoriteBinding
import com.example.githubuserapp.ui.ListUserAdapter
import com.example.githubuserapp.ui.ViewModelFactory
import com.example.githubuserapp.ui.settings.SettingsActivity

class FavoriteActivity : AppCompatActivity() {

    private var binding: ActivityFavoriteBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.title = "Favorite User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val favoriteViewModel by viewModels<FavoriteUserViewModel> {
            ViewModelFactory.getInstance(application)
        }

        val layoutManager = LinearLayoutManager(this)
        binding?.rvListUser?.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding?.rvListUser?.addItemDecoration(itemDecoration)

        val adapter = ListUserAdapter()

        favoriteViewModel.getFavoriteUser().observe(this) { user ->
            val items = arrayListOf<ItemsItem>()
            user.map {
                val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl!!)
                items.add(item)
            }
            adapter.submitList(items)
            binding?.rvListUser?.adapter = adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favorite, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.action_settings -> {
                val intent = Intent(this@FavoriteActivity, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}