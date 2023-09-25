package com.example.githubuserapp.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserapp.R
import com.example.githubuserapp.data.database.FavoriteUser
import com.example.githubuserapp.data.response.DetailUserResponse
import com.example.githubuserapp.databinding.ActivityDetailUserBinding
import com.example.githubuserapp.ui.FollowFragment
import com.example.githubuserapp.ui.SectionsPagerAdapter
import com.example.githubuserapp.ui.ViewModelFactory
import com.example.githubuserapp.ui.favorite.FavoriteActivity
import com.example.githubuserapp.ui.favorite.FavoriteUserViewModel
import com.example.githubuserapp.ui.settings.SettingsActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private var binding: ActivityDetailUserBinding? = null
    private var isFavorite: Boolean = false
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.title = getString(R.string.detail_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val favoriteViewModel by viewModels<FavoriteUserViewModel> {
            ViewModelFactory.getInstance(application)
        }

        detailViewModel.userDetail.observe(this) { user ->
            setDetailUser(user)
        }

        detailViewModel.isNoData.observe(this){
            it.getContentIfNotHandled()?.let {
                Snackbar.make(
                    window.decorView.rootView,
                    "User Not Found!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        if (savedInstanceState == null) {
            detailViewModel.isLoading.observe(this) {
                showLoading(it)
            }
        }

        val username = intent.getStringExtra(FollowFragment.ARG_USERNAME)
        val sectionPagerAdapter = SectionsPagerAdapter(this, username.toString())
        val viewPager: ViewPager2? = binding?.viewPager
        viewPager?.adapter = sectionPagerAdapter

        val tabs: TabLayout? = binding?.tabs
        if (tabs != null && viewPager != null) {
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()

        }

        detailViewModel.findUserDetail(username.toString())

        binding?.fabFavorite?.setOnClickListener {
            val avatarUrl = binding?.tvPicture?.text.toString()
            val favoriteUser = FavoriteUser(username.toString(), avatarUrl)
            if (isFavorite) {
                favoriteViewModel.delete(favoriteUser)
                Toast.makeText(this, "Removed From Favorite!", Toast.LENGTH_SHORT).show()
            } else {
                favoriteViewModel.insert(favoriteUser)
                Toast.makeText(this, "Added To Favorite!", Toast.LENGTH_SHORT).show()
            }
        }

        favoriteViewModel.getFavoriteUserByUsername(username.toString()).observe(this) {
            isFavorite = if (it?.username != null) {
                binding?.fabFavorite?.setImageResource(R.drawable.baseline_favorite_24)
                true
            } else {
                binding?.fabFavorite?.setImageResource(R.drawable.baseline_favorite_border_24)
                false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail_user, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this@DetailUserActivity, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.action_share -> {
                val message = getString(
                    R.string.share_message,
                    binding?.tvName?.text,
                    binding?.tvUsername?.text,
                    binding?.tvFollowers?.text,
                    binding?.tvFollowing?.text
                )
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, message)
                startActivity(Intent.createChooser(shareIntent, null))
            }
            android.R.id.home -> {
                finish()
            }
            R.id.action_favorite -> {
                val intent = Intent(this@DetailUserActivity, FavoriteActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setDetailUser(detail: DetailUserResponse){
        val url = "https://avatars.githubusercontent.com/u/${detail.id}?v=4"
        binding?.apply {
            tvName.text = detail.name
            tvName.text = detail.name
            tvUsername.text = detail.login
            tvPicture.text = url
            tvFollowers.text = getString(R.string.followers_count, detail.followers.toString())
            tvFollowing.text = getString(R.string.following_count, detail.following.toString())
            Glide.with(ivPicture)
                .load(url)
                .into(ivPicture)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.isVisible = isLoading
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}