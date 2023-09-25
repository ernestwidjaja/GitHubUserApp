package com.example.githubuserapp.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuserapp.data.UserRepository
import com.example.githubuserapp.data.database.FavoriteUser
import kotlinx.coroutines.launch

class FavoriteUserViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getFavoriteUser(): LiveData<List<FavoriteUser>> = userRepository.getFavoriteUser()

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> = userRepository.getFavoriteUserByUsername(username)

    fun insert(favoriteUser: FavoriteUser) = viewModelScope.launch {
            userRepository.insert(favoriteUser)
        }

    fun delete(favoriteUser: FavoriteUser) = viewModelScope.launch {
            userRepository.delete(favoriteUser)
        }
}