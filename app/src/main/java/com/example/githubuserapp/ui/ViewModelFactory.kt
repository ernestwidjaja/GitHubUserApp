package com.example.githubuserapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuserapp.data.UserRepository
import com.example.githubuserapp.di.Injection
import com.example.githubuserapp.ui.favorite.FavoriteUserViewModel
import com.example.githubuserapp.ui.settings.SettingsPreferences
import com.example.githubuserapp.ui.settings.SettingsViewModel
import com.example.githubuserapp.ui.settings.dataStore

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val pref: SettingsPreferences
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteUserViewModel::class.java)) {
            return FavoriteUserViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(pref) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context) : ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context), SettingsPreferences.getInstance(context.dataStore))
            }. also { instance = it }
    }
}